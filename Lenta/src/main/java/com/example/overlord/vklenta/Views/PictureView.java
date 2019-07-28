package com.example.overlord.vklenta.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.overlord.vklenta.Activity.PictureActivity;
import com.example.overlord.vklenta.DownloadImageTask;
import com.example.overlord.vklenta.Model.Picture;
import com.example.overlord.vklenta.Model.PictureModel;
import com.example.overlord.vklenta.Model.Post;

import java.util.ArrayList;

/**
 * Created by OverLord on 30.03.2018.
 */

public class PictureView extends PictureModelView {
    private Post perentPost;

    public PictureView(Context context) {
        super(context);
    }

    public PictureView(Context context, Picture pictureModel, @Nullable Post perentPost) {
        super(context, pictureModel);
        this.perentPost = perentPost;
    }

    public PictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PictureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void initOnClickListener(){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(getContext() instanceof PictureActivity) && perentPost!=null) {
                    Intent intent = new Intent(getContext(), PictureActivity.class);
                    ArrayList<Integer> IDs = new ArrayList<>();
                    for (int i=0;i<perentPost.getPhotos().size();i++) {
                        IDs.add(perentPost.getPhotos().get(i).getID());
                        if (perentPost.getPhotos().get(i).getID() == getID()) {
                            intent.putExtra("currentIndex", i);
                        }
                    }
                    intent.putExtra("pictureIDs", IDs);
                    getContext().startActivity(intent);
                }
            }
        });
    }
}
