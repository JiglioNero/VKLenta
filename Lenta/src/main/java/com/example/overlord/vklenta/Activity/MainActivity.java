package com.example.overlord.vklenta.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.overlord.vklenta.DataObserver;
import com.example.overlord.vklenta.Model.Post;
import com.example.overlord.vklenta.PostAdapter;
import com.example.overlord.vklenta.R;
import com.example.overlord.vklenta.VKRequestController;
import com.google.gson.Gson;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends RefreshUIActivity {
    private String nextFrom;
    private int startTime;
    private ArrayList<String> typesForRequest;

    private SwipeRefreshLayout swipe;
    private PostAdapter adapter;
    private ListView newsList;
    private FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        login();
    }

    private void login() {
        if (!VKSdk.isLoggedIn()) {
            VKSdk.login(this, new String[]{VKScope.WALL, VKScope.FRIENDS, VKScope.VIDEO});
        }
    }

    private void init() {
        typesForRequest = new ArrayList<>();
        typesForRequest.add("post");
        typesForRequest.add("friend");
        typesForRequest.add("photo");
        typesForRequest.add("wall_photo");
        swipe = findViewById(R.id.swipeRefresh);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRequest(-1, Dimension.Up);
            }
        });
        adapter = new PostAdapter(MainActivity.this);
        newsList = findViewById(R.id.newsList);
        newsList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!swipe.isRefreshing() && firstVisibleItem > totalItemCount - visibleItemCount - 10) {
                    doRequest(20, Dimension.Down);
                }
            }
        });
        newsList.setAdapter(adapter);
        actionButton = findViewById(R.id.floatingActionButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new PostAdapter(MainActivity.this);
                newsList.setAdapter(adapter);
                nextFrom = new String();
                startTime = 0;
                doRequest(20, Dimension.Down);
            }
        });
    }

    private void doRequest(int count, Dimension dim) {
        swipe.setRefreshing(true);
        final Dimension dimension = dim;

        HashMap<String, Object> param = new HashMap<>();

        if (count != -1) {
            param.put("count", count);
        }
        if (dimension == Dimension.Down) {
            param.put("start_from", nextFrom);
        }
        if (startTime > 0 && dimension == Dimension.Up) {
            param.put("start_time", startTime);
        }
        String filters = new String();
        for (int i = 0; i < typesForRequest.size(); i++) {
            if (i != 0) {
                filters += ",";
            }
            filters += typesForRequest.get(i);
        }

        param.put("filters", filters);

        VKRequest request = new VKRequest("newsfeed.get", new VKParameters(param));
        request.setRequestListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject attr = response.json.getJSONObject("response");

                    for (int i = 0; i < attr.getJSONArray("items").length(); i++) {
                        Post post = DataObserver.getInstance().parseForPost(attr.getJSONArray("items").getJSONObject(i), attr.getJSONArray("groups"), attr.getJSONArray("profiles"));
                        if (adapter.addPost(post)) {
                            newsList.addFooterView(new View(MainActivity.this));
                        }
                    }
                    adapter.sortByDate();
                    newsList.refreshDrawableState();

                    System.out.println("TYPES :");
                    for(String s : typesForRequest){
                        System.out.println(s);
                    }

                    System.out.println("ADAPTER :"+adapter.getCount());
                    System.out.println("LIST :"+newsList.getCount());

                    if (dimension == Dimension.Up) {
                        newsList.smoothScrollToPosition(0);
                    }

                    nextFrom = (String) response.json.getJSONObject("response").get("next_from");
                    int date = Integer.parseInt(String.valueOf(response.json.getJSONObject("response").getJSONArray("items").getJSONObject(0).get("date")));
                    if (startTime > date) {
                        startTime = date;
                    } else {
                        if (startTime == date) {
                            swipe.setRefreshing(false);
                            return;
                        }
                    }
                    swipe.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    swipe.setRefreshing(false);
                }
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                swipe.setRefreshing(false);
            }
        });
        VKRequestController.getInstance().addRequest(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.logOut).setTitle(getResources().getString(R.string.logOut));
        menu.findItem(R.id.changeLan).setTitle(getResources().getString(R.string.changeLanguage));
        menu.findItem(R.id.chooseTypes).setTitle(getResources().getString(R.string.chooseTypesOfPosts));
        menu.findItem(R.id.rusL).setTitle(getResources().getString(R.string.russianL));
        menu.findItem(R.id.engL).setTitle(getResources().getString(R.string.englishL));
        menu.findItem(R.id.posts).setTitle(getResources().getString(R.string.posts));
        menu.findItem(R.id.newFriends).setTitle(getResources().getString(R.string.newFriends));
        menu.findItem(R.id.newVideo).setTitle(getResources().getString(R.string.newVideo));
        menu.findItem(R.id.newPhoto).setTitle(getResources().getString(R.string.newPhoto));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getResources().getString(R.string.logOut))) {
            VKSdk.logout();
            adapter = new PostAdapter(MainActivity.this);
            newsList.setAdapter(adapter);
            nextFrom = new String();
            startTime = 0;
            DataObserver.getInstance().clearChache();
            login();
        }
        if (item.getTitle().equals(getResources().getString(R.string.russianL))) {
            changeLanguage(new Locale("ru"));
        }
        if (item.getTitle().equals(getResources().getString(R.string.englishL))) {
            changeLanguage(new Locale("eng"));
        }
        if (item.getTitle().equals(getResources().getString(R.string.posts))) {
            if (item.isChecked()) {
                typesForRequest.remove("post");
                item.setChecked(false);
            } else {
                typesForRequest.add("post");
                item.setChecked(true);
            }
            actionButton.callOnClick();
        }
        if (item.getTitle().equals(getResources().getString(R.string.newFriends))) {
            if (item.isChecked()) {
                typesForRequest.remove("friend");
                item.setChecked(false);
            } else {
                typesForRequest.add("friend");
                item.setChecked(true);
            }
            actionButton.callOnClick();
        }
        if (item.getTitle().equals(getResources().getString(R.string.newPhoto))) {
            if (item.isChecked()) {
                typesForRequest.remove("photo");
                typesForRequest.remove("wall_photo");
                item.setChecked(false);
            } else {
                typesForRequest.add("photo");
                typesForRequest.add("wall_photo");
                item.setChecked(true);
            }
            actionButton.callOnClick();
        }
        if (item.getTitle().equals(getResources().getString(R.string.newVideo))) {
            if (item.isChecked()) {
                typesForRequest.remove("video");
                item.setChecked(false);
            } else {
                typesForRequest.add("video");
                item.setChecked(true);
            }
            actionButton.callOnClick();
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeLanguage(Locale locale) {
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        DataObserver.getInstance().getBaseContext().getResources().updateConfiguration(configuration, null);
        getBaseContext().getResources().updateConfiguration(configuration, null);
        setTitle(getResources().getString(R.string.app_name));

        ArrayList<Post> chache = DataObserver.getInstance().getPosts();
        for (Post post : chache) {
            post.refreshLanguage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_LONG).show();
                doRequest(20, Dimension.Down);
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private enum Dimension {
        Up, Down
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("listPosition", newsList.getLastVisiblePosition());
        outState.putString("nextFrom", nextFrom);
        outState.putInt("startTime", startTime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nextFrom = savedInstanceState.getString("nextFrom");
        startTime = savedInstanceState.getInt("startTime");
        int countOfPosts = adapter.addAll(DataObserver.getInstance().getPosts());
        for (int i = 0; i < countOfPosts; i++) {
            newsList.addFooterView(new View(MainActivity.this));
        }
        newsList.smoothScrollToPosition(savedInstanceState.getInt("listPosition"));
    }
}
