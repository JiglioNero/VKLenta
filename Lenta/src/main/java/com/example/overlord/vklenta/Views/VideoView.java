package com.example.overlord.vklenta.Views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.example.overlord.vklenta.Model.Video;
import com.example.overlord.vklenta.R;

/**
 * Created by OverLord on 17.04.2018.
 */

public class VideoView extends PictureModelView {
    private ImageView playButton;

    public VideoView(Context context) {
        super(context);
    }

    public VideoView(Context context, Video pictureModel) {
        super(context, pictureModel);
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        playButton = new ImageView(getContext());
        playButton.setImageDrawable(getResources().getDrawable(R.drawable.play_button1));
        addView(playButton,new LayoutParams(200, 200, Gravity.CENTER));
        playButton.setVisibility(INVISIBLE);
    }

    @Override
    public void showProgressBar() {
        super.showProgressBar();
        if (playButton!=null) {
            playButton.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void hideProgressBar() {
        super.hideProgressBar();
        if (playButton!=null) {
            playButton.setVisibility(VISIBLE);
        }
    }

    @Override
    protected void initOnClickListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(((Video)picture).getVideoUrl()));
                getContext().startActivity(intent);
            }
        });
    }
}
