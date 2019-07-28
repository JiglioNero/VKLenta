package com.example.overlord.vklenta.Model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.overlord.vklenta.Activity.MainActivity;
import com.example.overlord.vklenta.Activity.PostActivity;
import com.example.overlord.vklenta.DataObserver;
import com.example.overlord.vklenta.R;
import com.example.overlord.vklenta.Views.AudioView;
import com.example.overlord.vklenta.Views.DocView;
import com.example.overlord.vklenta.Views.PictureView;
import com.example.overlord.vklenta.Views.VideoView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by OverLord on 11.03.2018.
 */

public class Post implements GiveIdAble {
    private int id;
    private View parentView;
    private Type type;

    private PictureModel groupAvatar;
    private String groupName;
    private Date date;
    private String text;
    private ArrayList<PictureModel> photos;
    private ArrayList<PictureModel> videos;
    private ArrayList<AudioModel> music;
    private ArrayList<DocModel> docs;

    public Post(Type type, HashMap<String, Object> attachment) {
        this.type = type;
        groupAvatar = (PictureModel) attachment.get("groupAvatar");
        groupName = (String) attachment.get("groupName");
        text = (String) attachment.get("text");
        photos = (ArrayList<PictureModel>) attachment.get("photo");
        videos = (ArrayList<PictureModel>) attachment.get("video");
        music = (ArrayList<AudioModel>) attachment.get("music");
        docs = (ArrayList<DocModel>) attachment.get("docs");
        id = (int) attachment.get("id");

        date = new Date((long) ((int) attachment.get("date")) * 1000);
    }

    public View fillView(Context context, View view, ViewGroup parent, boolean needToSinkWithChache) {
        LayoutInflater lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = lInflater.inflate(R.layout.post, parent, false);

        ((TextView) view.findViewById(R.id.groupName)).setText(getGroupName());
        ((TextView) view.findViewById(R.id.dateOfPost)).setText(new SimpleDateFormat("dd MMMM YYYY в H : mm", new DateFormatSymbols() {
            @Override
            public String[] getMonths() {
                return new String[]{"января", "февраля", "марта", "апреля", "мая", "июня",
                        "июля", "августа", "сентября", "октября", "ноября", "декабря"};
            }
        }).format(getDate()));
        ((TextView) view.findViewById(R.id.textOfPost)).setText(getText());

        PictureView avatar = (PictureView) view.findViewById(R.id.groupAvatar);
        if (getGroupAvatar() instanceof Picture) {
            avatar.setPicture((Picture) getGroupAvatar());
        } else {
            if (getGroupAvatar() instanceof PictureView) {
                avatar.setPicture(((PictureView) getGroupAvatar()).getPictureModel());
            }
        }

        ((LinearLayout) view.findViewById(R.id.attachmentSet)).removeAllViews();

        if (getPhotos() != null) {
            for (int i = 0; i < getPhotos().size(); i++) {
                PictureView pictureV;
                if (getPhotos().get(i) instanceof Picture) {
                    if (needToSinkWithChache) {
                        pictureV = (PictureView) DataObserver.getInstance().replaceWithViewInChache(new PictureView(view.getContext(), ((Picture) getPhotos().get(i)), this));
                    } else {
                        pictureV = new PictureView(context, (Picture) getPhotos().get(i), this);
                    }
                    getPhotos().remove(i);
                    getPhotos().add(i, pictureV);
                } else {
                    pictureV = (PictureView) getPhotos().get(i);
                }
                if (pictureV.getParent() != null) {
                    ((ViewGroup) pictureV.getParent()).removeView(pictureV);
                }
                ((LinearLayout) view.findViewById(R.id.attachmentSet)).addView(pictureV, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }

        if (getVideos() != null) {
            for (int i = 0; i < getVideos().size(); i++) {
                VideoView videoV;
                if (getVideos().get(i) instanceof Video) {
                    if (needToSinkWithChache) {
                        videoV = (VideoView) DataObserver.getInstance().replaceWithViewInChache(new VideoView(view.getContext(), ((Video) getVideos().get(i))));
                    } else {
                        videoV = new VideoView(context, (Video) getVideos().get(i));
                    }
                    getVideos().remove(i);
                    getVideos().add(i, videoV);
                } else {
                    videoV = (VideoView) getVideos().get(i);
                }
                if (videoV.getParent() != null) {
                    ((ViewGroup) videoV.getParent()).removeView(videoV);
                }
                ((LinearLayout) view.findViewById(R.id.attachmentSet)).addView(videoV, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }

        if (getMusic() != null) {
            for (int i = 0; i < getMusic().size(); i++) {
                AudioView audioV;
                if (getMusic().get(i) instanceof Audio) {
                    if (needToSinkWithChache) {
                        audioV = (AudioView) DataObserver.getInstance().replaceWithViewInChache(new AudioView(view.getContext(), (Audio) getMusic().get(i)));
                    } else {
                        audioV = new AudioView(context, (Audio) getMusic().get(i));
                    }
                    getMusic().remove(i);
                    getMusic().add(i, audioV);
                } else {
                    audioV = (AudioView) getMusic().get(i);
                }
                if (audioV.getParent() != null) {
                    ((ViewGroup) audioV.getParent()).removeView(audioV);
                }
                ((LinearLayout) view.findViewById(R.id.attachmentSet)).addView(audioV, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }

        if (getDocs() != null) {
            for (int i = 0; i < getDocs().size(); i++) {
                DocView docV;
                if (getDocs().get(i) instanceof Doc) {
                    if (needToSinkWithChache) {
                        docV = (DocView) DataObserver.getInstance().replaceWithViewInChache(new DocView(view.getContext(), (Doc) getDocs().get(i)));
                    } else {
                        docV = new DocView(context, (Doc) getDocs().get(i));
                    }
                    getDocs().remove(i);
                    getDocs().add(i, docV);
                } else {
                    docV = (DocView) getDocs().get(i);
                }
                if (docV.getParent() != null) {
                    ((ViewGroup) docV.getParent()).removeView(docV);
                }
                ((LinearLayout) view.findViewById(R.id.attachmentSet)).addView(docV, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContext() instanceof MainActivity) {
                    Intent intent = new Intent(v.getContext(), PostActivity.class);
                    intent.putExtra("postId", getID());
                    v.getContext().startActivity(intent);
                }
            }
        });

        parentView = view;

        return view;
    }

    public void refreshLanguage() {
        boolean flag = false;
        switch (type) {
            case NEW_FRIEND:
                text = DataObserver.getInstance().getResources().getString(R.string.friendGreetings);
                flag = true;
                break;
            case NEW_PHOTO:
                text = photos.size() + " " + DataObserver.getInstance().getResources().getString(R.string.textForNewPhoto);
                flag = true;
                break;
            case NEW_VIDEO:
                text = videos.size() + " " + DataObserver.getInstance().getResources().getString(R.string.textForNewVideo);
                flag = true;
                break;
        }
        if (flag && parentView!=null){
            ((TextView) parentView.findViewById(R.id.textOfPost)).setText(getText());
        }
    }

    public ArrayList<DocModel> getDocs() {
        return docs;
    }

    public ArrayList<AudioModel> getMusic() {
        return music;
    }

    public ArrayList<PictureModel> getVideos() {
        if (videos == null) {
            videos = new ArrayList<>();
        }
        return videos;
    }

    public synchronized void addVideo(PictureModel video, boolean needToSinkWithChache) {
        if (videos == null) {
            videos = new ArrayList<>();
        }
        videos.add(video);

        if (getParentView() != null && getParentView().getParent() != null) {
            VideoView videoV;
            if (video instanceof Video) {
                if (needToSinkWithChache) {
                    videoV = (VideoView) DataObserver.getInstance().replaceWithViewInChache(new VideoView(getParentView().getContext(), (Video) video));
                } else {
                    videoV = new VideoView(getParentView().getContext(), (Video) video);
                }
                getVideos().remove(video);
                getVideos().add(videoV);
            } else {
                videoV = (VideoView) video;
            }
            if (videoV.getParent() != null) {
                ((ViewGroup) videoV.getParent()).removeView(videoV);
            }
            ((LinearLayout) getParentView().findViewById(R.id.attachmentSet)).addView(videoV, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            parentView.postInvalidate();
        }

    }

    public PictureModel getGroupAvatar() {
        return groupAvatar;
    }

    public String getGroupName() {
        return groupName;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public ArrayList<PictureModel> getPhotos() {
        return photos;
    }

    public View getParentView() {
        return parentView;
    }


    @Override
    public int getID() {
        return id;
    }

    public enum Type {
        NEW_FRIEND, POST, NEW_VIDEO, NEW_PHOTO
    }
}
