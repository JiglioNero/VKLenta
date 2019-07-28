package com.example.overlord.vklenta.Views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.overlord.vklenta.Model.Doc;
import com.example.overlord.vklenta.Model.DocModel;
import com.example.overlord.vklenta.R;

/**
 * Created by OverLord on 24.04.2018.
 */

public class DocView extends CardView implements DocModel {
    private Doc doc;
    private LinearLayout layout;

    public DocView(Context context) {
        super(context);
        init();
    }

    public DocView(Context context, Doc doc) {
        super(context);
        this.doc = doc;
        init();

        TextView title = new TextView(getContext());
        title.setText(doc.getName());
        title.setTextSize(20);
        layout.addView(title, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public DocView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DocView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        ImageView icon = new ImageView(getContext());
        icon.setImageDrawable(getResources().getDrawable(R.drawable.attachment));

        layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);

        layout.addView(icon,new LayoutParams(150,150));

        addView(layout);
    }

    @Override
    public String getName() {
        return doc.getName();
    }

    @Override
    public int getID() {
        return doc.getID();
    }
}
