package com.example.overlord.vklenta.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.example.overlord.vklenta.DataObserver;
import com.example.overlord.vklenta.Model.Post;
import com.example.overlord.vklenta.R;

/**
 * Created by OverLord on 11.04.2018.
 */

public class PostActivity extends RefreshUIActivity {
    private Post post;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity);
        if (getIntent().getExtras().containsKey("postId")){
            post = (Post) DataObserver.getInstance().get((Integer) getIntent().getExtras().get("postId"));
        }
        ((LinearLayout)findViewById(R.id.postPlace)).addView(post.fillView(this,null, findViewById(R.id.postPlace),false));
    }
}
