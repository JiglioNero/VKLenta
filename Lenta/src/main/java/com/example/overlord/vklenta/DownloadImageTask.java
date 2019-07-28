package com.example.overlord.vklenta;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.overlord.vklenta.Activity.RefreshUIActivity;
import com.example.overlord.vklenta.Views.PictureModelView;

import java.io.InputStream;

/**
 * Created by OverLord on 03.04.2018.
 */

public class DownloadImageTask extends AsyncTask<Void, ImageView, Bitmap> {
    private PictureModelView pictureModelView;

    public DownloadImageTask(PictureModelView pictureModelView) {
        this.pictureModelView = pictureModelView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pictureModelView.showProgressBar();
    }

    protected Bitmap doInBackground(Void... voids) {
        Bitmap pic = null;
        if (!DataObserver.getInstance().conteinsId(pictureModelView.getID()) || pictureModelView.getImage() == null || pictureModelView.getPreview() == null) {
            try {
                if (pictureModelView.getPreview() == null) {
                    InputStream inPrev = new java.net.URL(pictureModelView.getPreviewUrl()).openStream();
                    pic = BitmapFactory.decodeStream(inPrev);
                    pictureModelView.setPreview(pic);
                } else {
                    InputStream inIm = new java.net.URL(pictureModelView.getImageUrl()).openStream();
                    pic = BitmapFactory.decodeStream(inIm);
                    pictureModelView.setImage(pic);
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            DataObserver.getInstance().addToChache(pictureModelView);
        } else {
            pic = pictureModelView.getImage();
        }
        return pic;
    }

    protected void onPostExecute(Bitmap result) {
        if (pictureModelView.getContext() instanceof RefreshUIActivity) {
            ((RefreshUIActivity) pictureModelView.getContext()).refreshImageUI(pictureModelView);
        }
        if (pictureModelView.getImage() != null) {
            pictureModelView.hideProgressBar();
        }
        else {
            new DownloadImageTask(pictureModelView).execute();
        }
    }
}