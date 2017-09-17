package com.xiaomi.mitv.store.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tv.ui.metro.sampleapp.R;

/**
 * Created by TV Metro on 9/1/14.
 */
public class TitleBar extends LinearLayout {

    private TextView mTitleView;
    public TitleBar(Context context) {
        this(context, null, 0);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int UIStyle) {
        super(context, attrs, UIStyle);
        init(context);
    }

    public void setTitle(int resid){
        mTitleView.setText(resid);
    }

    public void setTitle(CharSequence text){
        mTitleView.setText(text);
    }

    private OnClickListener mClickListner;
    public void setBackPressListner(OnClickListener clickListener){
        mClickListner = clickListener;
        findViewById(R.id.back_imageview).setOnClickListener(mClickListner);
    }

    private void init(Context context){
        LayoutInflater.from(getContext()).inflate(R.layout.title_bar_layout, this);
        mTitleView = (TextView) this.findViewById(R.id.titlebar_title);
    }
}
