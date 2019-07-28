package com.example.overlord.vklenta;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.example.overlord.vklenta.Activity.MainActivity;
import com.example.overlord.vklenta.Model.Audio;
import com.example.overlord.vklenta.Model.AudioModel;
import com.example.overlord.vklenta.Model.Doc;
import com.example.overlord.vklenta.Model.DocModel;
import com.example.overlord.vklenta.Model.GiveIdAble;
import com.example.overlord.vklenta.Model.Picture;
import com.example.overlord.vklenta.Model.PictureModel;
import com.example.overlord.vklenta.Model.Post;
import com.example.overlord.vklenta.Model.Video;
import com.example.overlord.vklenta.Views.PictureModelView;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by OverLord on 22.03.2018.
 */

public class DataObserver extends android.app.Application {

    private static DataObserver instance = null;

    private HashMap<Integer, GiveIdAble> chache = new HashMap<>();

    public DataObserver() {
        if (instance == null) {
            instance = this;
        } else {
            chache = instance.getChache();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
            @Override
            public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
                if (newToken == null) {
                    Intent intent = new Intent(DataObserver.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
    }

    public void clearChache(){
        chache.clear();
    }

    public GiveIdAble get(int id) {
        return chache.get(id);
    }

    public boolean conteinsId(int id) {
        return chache.containsKey(id);
    }

    public static DataObserver getInstance() {
        if (instance == null)
            instance = new DataObserver();
        return instance;
    }

    public ArrayList<Post> getPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        for (Map.Entry<Integer, GiveIdAble> pair : chache.entrySet()) {
            if (pair.getValue() instanceof Post) {
                posts.add((Post) pair.getValue());
            }
        }
        return posts;
    }

    public HashMap<Integer, GiveIdAble> getChache() {
        return chache;
    }

    public GiveIdAble addToChache(GiveIdAble object) {
        if (chache.containsKey(object.getID())) {
            return chache.get(object.getID());
        } else {
            chache.put(object.getID(), object);
            return object;
        }
    }

    public void addToChache(HashMap<Integer, GiveIdAble> data) {
        for (Map.Entry<Integer, GiveIdAble> object : data.entrySet()) {
            if (!chache.containsKey(object.getKey())) {
                chache.put(object.getKey(), object.getValue());
            }
        }
    }

    public GiveIdAble replaceWithViewInChache(GiveIdAble object) {
        if (chache.containsKey(object.getID())) {
            GiveIdAble o = chache.get(object.getID());
            if (o instanceof View) {
                return o;
            } else {
                chache.put(object.getID(), object);
                return object;
            }
        }
        return addToChache(object);
    }

    public Post parseForPost(JSONObject item, JSONArray groups, JSONArray profiles) throws Exception {
        String type = (String) item.get("type");
        if (type.equals("wall_photo")){
            type = "photo";
        }
        int id = 0;

        switch (type) {
            case "friend":
                id = item.getInt("source_id");
                break;
            case "video":
                JSONArray videos = item.getJSONObject("video").getJSONArray("items");
                for (int i = 0; i < videos.length(); i++) {
                    id -= videos.getJSONObject(i).getInt("id");
                }
                id += item.getInt("date");
                break;
            case "photo":
                JSONArray pictures = item.getJSONObject("photos").getJSONArray("items");
                for (int i = 0; i < pictures.length(); i++) {
                    id -= pictures.getJSONObject(i).getInt("id");
                }
                id += item.getInt("date");
                id += item.getInt("post_id");
                break;
            default:
                id = item.getInt("post_id");
        }
        if (id == 0) {
            throw new Exception("ID HAS NOT FOUND!");
        }
        if (!chache.containsKey(id)) {
            HashMap<String, Object> attachment = new HashMap<>();

            ArrayList<PictureModel> photoArray = new ArrayList<>();
            ArrayList<VideoParseTask> videoParseTasks = new ArrayList<>();
            ArrayList<AudioModel> music = new ArrayList<>();
            ArrayList<DocModel> docs = new ArrayList<>();

            loop:
            {
                for (int i = 0; i < groups.length(); i++) {
                    if (groups.getJSONObject(i).get("id").equals(Math.abs((Integer) item.get("source_id")))) {
                        attachment.put("groupName", groups.getJSONObject(i).get("name"));
                        attachment.put("groupAvatar", addToChache(new Picture(groups.getJSONObject(i).getInt("id") + 1, (String) groups.getJSONObject(i).get("photo_200"), (String) groups.getJSONObject(i).get("photo_50"))));
                        break loop;
                    }
                }
                for (int i = 0; i < profiles.length(); i++) {
                    if (profiles.getJSONObject(i).get("id").equals(Math.abs((Integer) item.get("source_id")))) {
                        attachment.put("groupName", profiles.getJSONObject(i).get("first_name") + " " + profiles.getJSONObject(i).get("last_name"));
                        attachment.put("groupAvatar", addToChache(new Picture(profiles.getJSONObject(i).getInt("id") + 1, (String) profiles.getJSONObject(i).get("photo_100"), (String) profiles.getJSONObject(i).get("photo_50"))));
                        break loop;
                    }
                }
            }

            attachment.put("id", id);
            attachment.put("date", item.get("date"));

            if (!item.isNull("text")) {
                attachment.put("text", item.getString("text"));
            }


            Post.Type typeOfPost = null;

            switch (type) {
                case "friend":
                    typeOfPost = Post.Type.NEW_FRIEND;
                    attachment.put("text", getResources().getString(R.string.friendGreetings));
                    break;
                case "photo":
                    typeOfPost = Post.Type.NEW_PHOTO;
                    JSONArray photos = item.getJSONObject("photos").getJSONArray("items");
                    attachment.put("text", photos.length() + " " + getResources().getString(R.string.textForNewPhoto));
                    for (int i = 0; i < photos.length(); i++) {
                        photoArray.add((PictureModel) addToChache(new Picture((Integer) photos.getJSONObject(i).get("id"), getPhotoUrl(photos.getJSONObject(i)), getPreviewUrl(photos.getJSONObject(i)))));
                    }
                    break;
                case "video":
                    typeOfPost = Post.Type.NEW_VIDEO;
                    attachment.put("text", item.getJSONObject("video").getInt("count") + " " + getResources().getString(R.string.textForNewVideo));
                    videoParseTasks.add(new VideoParseTask(id, item));
                    break;
                case "post":
                    typeOfPost = Post.Type.POST;
                    if (!item.isNull("attachments")) {
                        JSONArray attachments = item.getJSONArray("attachments");
                        for (int i = 0; i < attachments.length(); i++) {
                            String attachmentType = (String) attachments.getJSONObject(i).get("type");
                            JSONObject attachItem = attachments.getJSONObject(i).getJSONObject(attachmentType);

                            switch (attachmentType) {
                                case "photo":
                                    photoArray.add((PictureModel) addToChache(new Picture((Integer) attachItem.get("id"), getPhotoUrl(attachItem), getPreviewUrl(attachItem))));
                                    break;
                                case "video":
                                    videoParseTasks.add(new VideoParseTask(id, attachItem));
                                    break;
                                case "audio":
                                    music.add((AudioModel) addToChache(new Audio(attachItem.getInt("id"), attachItem.getString("artist"), attachItem.getString("title"), (String) attachItem.get("url"))));
                                    break;
                                case "doc":
                                    docs.add((DocModel) addToChache(new Doc(attachItem.getInt("id"), attachItem.getString("title"))));
                                    break;
                            }
                        }
                    }
                    break;
                default:
                    typeOfPost = Post.Type.POST;
            }

            attachment.put("docs", docs);
            attachment.put("music", music);
            attachment.put("photo", photoArray);

            Post result = (Post) addToChache(new Post(typeOfPost, attachment));
            for (VideoParseTask task : videoParseTasks) {
                task.execute();
            }

            return result;
        }
        return (Post) chache.get(id);

    }

    public class VideoParseTask extends AsyncTask<Void, Void, Void> {
        private int idOfPost;
        private JSONObject item;

        public VideoParseTask(int idOfPost, JSONObject item) {
            this.idOfPost = idOfPost;
            this.item = item;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String key = "";
                JSONArray videos = new JSONArray();
                if (!item.isNull("video")) {
                    videos = item.getJSONObject("video").getJSONArray("items");
                } else {
                    if (!(item.isNull("owner_id") && item.isNull("id"))) {
                        videos.put(item);
                    }
                }

                if (((Post) DataObserver.getInstance().get(idOfPost)).getVideos().size() < videos.length()) {
                    for (int i = 0; i < videos.length(); i++) {
                        if (i > 0)
                            key += ",";
                        key += String.valueOf(videos.getJSONObject(i).get("owner_id")) + "_" + String.valueOf(videos.getJSONObject(i).get("id"));
                        if (!videos.getJSONObject(i).isNull("access_key")) {
                            key += "_" + String.valueOf(videos.getJSONObject(i).get("access_key"));
                        }
                    }

                    HashMap<String, Object> param = new HashMap<>();

                    param.put("videos", key);

                    VKRequest request = new VKRequest("video.get", new VKParameters(param));
                    request.setRequestListener(new VKRequest.VKRequestListener() {
                        @Override
                        public void onComplete(VKResponse response) {
                            super.onComplete(response);
                            try {
                                JSONArray items = response.json.getJSONObject("response").getJSONArray("items");
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject video = items.getJSONObject(i);
                                    Picture picture = new Picture(video.getInt("id"), getPhotoUrl(video), getPreviewUrl(video));
                                    if (DataObserver.getInstance().get(idOfPost) instanceof Post) {
                                        ((Post) DataObserver.getInstance().get(idOfPost)).addVideo((PictureModel) addToChache(new Video(picture, video.getString("player"))), true);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    VKRequestController.getInstance().addRequest(request);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private String getPhotoUrl(JSONObject photo) throws JSONException {
        int quality = 1280;
        if (!photo.isNull("photo_" + quality)) {
            return (String) photo.get("photo_" + quality);
        }
        quality = 807;
        if (!photo.isNull("photo_" + quality)) {
            return (String) photo.get("photo_" + quality);
        }
        quality = 800;
        if (!photo.isNull("photo_" + quality)) {
            return (String) photo.get("photo_" + quality);
        }
        quality = 604;
        if (!photo.isNull("photo_" + quality)) {
            return (String) photo.get("photo_" + quality);
        }
        quality = 320;
        if (!photo.isNull("photo_" + quality)) {
            return (String) photo.get("photo_" + quality);
        }
        quality = 130;
        if (!photo.isNull("photo_" + quality)) {
            return (String) photo.get("photo_" + quality);
        }
        quality = 75;
        if (!photo.isNull("photo_" + quality)) {
            return (String) photo.get("photo_" + quality);
        }
        return null;
    }

    private String getPreviewUrl(JSONObject photo) throws JSONException {
        int quality = 75;
        if (!photo.isNull("photo_" + quality)) {
            return (String) photo.get("photo_" + quality);
        }
        quality = 130;
        if (!photo.isNull("photo_" + quality)) {
            return (String) photo.get("photo_" + quality);
        }
        quality = 320;
        if (!photo.isNull("photo_" + quality)) {
            return (String) photo.get("photo_" + quality);
        }
        quality = 604;
        if (!photo.isNull("photo_" + quality)) {
            return (String) photo.get("photo_" + quality);
        }
        quality = 800;
        if (!photo.isNull("photo_" + quality)) {
            return (String) photo.get("photo_" + quality);
        }
        quality = 807;
        if (!photo.isNull("photo_" + quality)) {
            return (String) photo.get("photo_" + quality);
        }
        quality = 1280;
        if (!photo.isNull("photo_" + quality)) {
            return (String) photo.get("photo_" + quality);
        }
        return null;
    }
}
