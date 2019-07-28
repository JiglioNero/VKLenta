package com.example.overlord.vklenta.Views;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.overlord.vklenta.Model.Audio;
import com.example.overlord.vklenta.Model.AudioModel;
import com.example.overlord.vklenta.R;

/**
 * Created by OverLord on 23.04.2018.
 */

public class AudioView extends CardView implements AudioModel {
    private Audio audio;

    private LinearLayout layout;

    public AudioView(Context context) {
        super(context);
        init();
    }

    public AudioView(Context context, Audio audio) {
        super(context);
        this.audio = audio;
        init();

        TextView title = new TextView(getContext());
        title.setText(audio.getNameOfSonger() + " - " + audio.getNameOfTrack());
        title.setTextSize(20);
        layout.addView(title, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public AudioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AudioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
        TextView title = new TextView(getContext());
        title.setText(audio.getNameOfSonger() + " - " + audio.getNameOfTrack());
        title.setTextSize(20);
        layout.addView(title, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void init() {
        if (audio != null) {
            ImageView playButton = new ImageView(getContext());
            playButton.setImageDrawable(getResources().getDrawable(R.drawable.play_button2));

            layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_VERTICAL);

            layout.addView(playButton, new LayoutParams(150, 150));

            addView(layout);

            /*setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    MediaPlayer mp = MediaPlayer.create(getContext(), Uri.parse(audio.getTrackUrl()));
                    mp.setLooping(false);
                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });

                }
            });
            */
        }
    }

    @Override
    public String getNameOfSonger() {
        return audio.getNameOfSonger();
    }

    @Override
    public String getNameOfTrack() {
        return audio.getNameOfTrack();
    }

    @Override
    public String getTrackUrl() {
        return audio.getTrackUrl();
    }

    @Override
    public int getID() {
        return audio.getID();
    }
}
