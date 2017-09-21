package com.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.yealink.MainApplication;
import com.yealink.NotifyService;
import com.yealink.lib.common.AbstractYealinkApplication;

/**
 * Created by Lenovo on 2017/5/11.
 */
public class MyApplication extends MainApplication {
    private static RequestQueue mQueue;
    // ceshi
    @Override
    public void onCreate() {
        super.onCreate();
        mQueue = Volley.newRequestQueue(getApplicationContext());
    }
    public static RequestQueue getHttpQueue(){
        return  mQueue;
    }
}
