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
 * Created by OverLord on 17.04.2018.
 */

public abstract class PictureModelView extends CardView implements PictureModel {
    protected ProgressBar bar;
    protected ImageView image;
    protected PictureModel picture;

    public PictureModelView(Context context) {
        super(context);
        init();
    }

    public PictureModelView(Context context, PictureModel pictureModel) {
        super(context);
        this.picture = pictureModel;
        init();
        new DownloadImageTask(this).execute();
    }

    public PictureModelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PictureModelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {

        bar = new ProgressBar(getContext());
        image = new ImageView(getContext());
        image.setAdjustViewBounds(true);

        addView(image, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(bar, new LayoutParams(100, 100, Gravity.CENTER));

        initOnClickListener();

        showProgressBar();
    }


    protected abstract void initOnClickListener();

    public PictureModel getPictureModel() {
        return picture;
    }

    public void setPicture(PictureModel pictureModel) {
        this.picture = pictureModel;
        new DownloadImageTask(this).execute();
    }

    public void refreshImage(){
        if (picture.getImage()!=null){
            setImageBitmap(picture.getImage());
        }
        else {
            if (picture.getPreview()!=null){
                setImageBitmap(picture.getPreview());
            }
        }
    }

    protected void setImageBitmap(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
        image.setMinimumHeight(bitmap.getHeight() * getWidth() / bitmap.getWidth());
        image.setMinimumWidth(getWidth());
        image.postInvalidate();
        this.postInvalidate();
    }

    public void showProgressBar() {
        bar.setVisibility(VISIBLE);
    }

    public void hideProgressBar() {
        bar.setVisibility(INVISIBLE);
    }

    @Override
    public int getID() {
        return picture.getID();
    }

    @Override
    public Bitmap getPreview() {
        return picture.getPreview();
    }

    @Override
    public void setPreview(Bitmap preview) {
        picture.setPreview(preview);
    }

    @Override
    public String getImageUrl() {
        return picture.getImageUrl();
    }

    @Override
    public String getPreviewUrl() {
        return picture.getPreviewUrl();
    }

    @Override
    public Bitmap getImage() {
        return picture.getImage();
    }

    @Override
    public void setImage(Bitmap image) {
        picture.setImage(image);
    }
}
