package com.example.overlord.vklenta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.overlord.vklenta.Model.GiveIdAble;
import com.example.overlord.vklenta.Model.Post;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by OverLord on 18.03.2018.
 */

public class PostAdapter extends BaseAdapter {
    private ArrayList<Post> feed;
    private Context cntx;

    public PostAdapter(Context cntx) {
        this.feed = new ArrayList<>();
        this.cntx = cntx;
    }

    public boolean addPost(Post post) {
        if (post != null && !containsPost(post.getID())) {
            feed.add(post);
            return true;
        }
        return false;
    }

    public void sortByDate(){
        Collections.sort(feed, (p2, p1) -> p1.getDate().compareTo(p2.getDate()));
    }

    public int addAll(ArrayList<Post> posts) {
        int count = 0;
        for (Post post : posts) {
            if (addPost(post)) {
                count++;
            }
        }
        return count;
    }

    private boolean containsPost(int id) {
        for (Post p : feed) {
            if (p.getID() == id)
                return true;
        }
        return false;
    }

    @Override
    public int getCount() {
        return feed.size();
    }

    @Override
    public Post getItem(int i) {
        return (Post) feed.toArray()[i];
    }

    @Override
    public long getItemId(int i) {
        return ((GiveIdAble) feed.toArray()[i]).getID();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        return getItem(i).fillView(cntx,convertView,parent,true);
    }
}
