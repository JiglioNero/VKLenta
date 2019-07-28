package com.example.overlord.vklenta.Activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.overlord.vklenta.DataObserver;
import com.example.overlord.vklenta.Model.GiveIdAble;
import com.example.overlord.vklenta.Model.Picture;
import com.example.overlord.vklenta.Model.PictureModel;
import com.example.overlord.vklenta.R;
import com.example.overlord.vklenta.Views.PictureView;

import java.util.ArrayList;

public class PictureActivity extends RefreshUIActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity);

        Intent intent = getIntent();

        ArrayList<Integer> pictureIDs = (ArrayList<Integer>) intent.getSerializableExtra("pictureIDs");
        ViewPager flipper = (ViewPager) findViewById(R.id.pictureContainer);
        ArrayList<PictureView> pictures = new ArrayList<>();

        for (int i = 0; i < pictureIDs.size(); i++) {
            GiveIdAble o = DataObserver.getInstance().get(pictureIDs.get(i));
            if (o instanceof PictureModel) {
                Picture picture;
                if (o instanceof Picture) {
                    picture = (Picture) o;
                } else {
                    if (o instanceof PictureView) {
                        picture = (Picture) ((PictureView) o).getPictureModel();
                    }
                    else {
                        continue;
                    }
                }
                PictureView pictureView = new PictureView(this, picture, null);
                pictures.add(pictureView);
            }
        }
        flipper.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pictures.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view.equals(object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(pictures.get(position), 0);
                return pictures.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(pictures.get(position));
            }
        });
        if (intent.getExtras().containsKey("currentIndex")) {
            flipper.setCurrentItem((Integer) intent.getExtras().get("currentIndex"));
        }
    }

}
