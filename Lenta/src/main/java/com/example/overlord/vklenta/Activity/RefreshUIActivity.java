package com.example.overlord.vklenta.Activity;

import android.support.v7.app.AppCompatActivity;

import com.example.overlord.vklenta.Views.PictureModelView;
import com.example.overlord.vklenta.Views.PictureView;

/**
 * Created by OverLord on 11.04.2018.
 */

public abstract class RefreshUIActivity extends AppCompatActivity {
    public void refreshImageUI(final PictureModelView pictureModelView) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pictureModelView.refreshImage();
            }
        });
    }
}
