package com.app;

import android.app.Application;
import android.content.res.AssetManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.yealink.MainApplication;
import com.yealink.NotifyService;
import com.yealink.lib.common.AbstractYealinkApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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

        File temp = new File("/data/data/" + this.getPackageName() + "/zshy_new.sqlite");
        if(!temp.exists()){
            copyFile("zshy_new.sqlite");
        }
    }
    public static RequestQueue getHttpQueue(){
        return  mQueue;
    }


    private void copyFile(String filename) {
        AssetManager assetManager = this.getAssets();

        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            String newFileName = "/data/data/" + this.getPackageName() + "/" + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }
}
