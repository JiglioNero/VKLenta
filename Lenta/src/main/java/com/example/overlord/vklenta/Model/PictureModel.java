package com.example.overlord.vklenta.Model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by OverLord on 03.04.2018.
 */

public interface PictureModel extends GiveIdAble{
    public Bitmap getPreview();

    public void setPreview(Bitmap preview);

    public String getImageUrl();

    public String getPreviewUrl();

    public Bitmap getImage();

    public void setImage(Bitmap image);
}
