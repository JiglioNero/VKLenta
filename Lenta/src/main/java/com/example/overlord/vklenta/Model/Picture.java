package com.example.overlord.vklenta.Model;


import android.graphics.Bitmap;

/**
 * Created by OverLord on 27.03.2018.
 */

public class Picture implements PictureModel {
    protected int id;
    private String imageUrl;
    private String previewUrl;
    private Bitmap image;
    private Bitmap preview;

    public Picture(int id, String imageUrl, String previewUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.previewUrl = previewUrl;
    }

    @Override
    public Bitmap getPreview() {
        return preview;
    }

    @Override
    public void setPreview(Bitmap preview) {
        this.preview = preview;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String getPreviewUrl() {
        return previewUrl;
    }

    @Override
    public Bitmap getImage() {
        return image;
    }

    @Override
    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public int getID() {
        return id;
    }
}
