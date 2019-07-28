package com.example.overlord.vklenta.Model;

import android.graphics.Bitmap;

/**
 * Created by OverLord on 17.04.2018.
 */

public class Video implements PictureModel {
    private Picture picture;
    private String videoUrl;

    public Video(Picture picture, String videoUrl) {
        this.picture = picture;
        this.videoUrl = videoUrl;
    }

    @Override
    public int getID() {
        return picture.getID();
    }

    public String getVideoUrl() {
        return videoUrl;
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
