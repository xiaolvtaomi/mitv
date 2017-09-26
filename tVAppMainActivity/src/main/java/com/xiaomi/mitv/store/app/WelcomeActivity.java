package com.xiaomi.mitv.store.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.VolleyHelper;

import com.tv.ui.metro.sampleapp.R;
import com.tv.ui.metro.utils.ViewUtils;

public class WelcomeActivity extends Activity {
    private ImageLoader mImageLoader;
    public String bg;
    RelativeLayout welcome_bg;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                Intent intent = new Intent(WelcomeActivity.this,AppMainActivity.class);
                startActivity(intent);
                finish();

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
      //  init();
       // SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        //取得欢迎界面背景
       // bg = sp.getString("user_bg", "");
        //mImageLoader.get(bg, ImageLoader.getCommonViewImageListener(welcome_bg, R.drawable.bg, R.drawable.bg));
        handler.sendEmptyMessageDelayed(1,1500 );
    }
//    public void  init(){
//        mImageLoader = VolleyHelper.getInstance(getApplicationContext().getApplicationContext()).getImageLoader();
//        welcome_bg= (RelativeLayout) findViewById(R.id.welcome_bg);
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewUtils.unbindDrawables(findViewById(R.id.welcome_bg));


    }

}
