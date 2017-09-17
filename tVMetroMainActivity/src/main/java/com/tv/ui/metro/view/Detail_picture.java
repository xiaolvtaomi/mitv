package com.tv.ui.metro.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.VolleyHelper;
import com.tv.ui.metro.R;

public class Detail_picture extends Activity {
    private ImageLoader mImageLoader;
    private String download_url_phone,app_pic,goods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_picture);
        mImageLoader = VolleyHelper.getInstance(getApplicationContext().getApplicationContext()).getImageLoader();
        ImageView qrCode= (ImageView) findViewById(R.id.iv_photo);
        Bundle bundle = this.getIntent().getExtras();
        //不加bundle非空判断，第一次启动，bundle报空指针
        if (bundle!=null) {
            download_url_phone=bundle.getString("download_url_phone");
            app_pic=bundle.getString("app_pic");
            goods=bundle.getString("goods");
        }
         if (!download_url_phone.equals(null)&&!"".equals(download_url_phone)) {
             mImageLoader.get(download_url_phone, ImageLoader.getCommonViewImageListener(qrCode, R.drawable.defeat, R.drawable.defeat));
         }
        if (!goods.equals(null)&&!"".equals(goods)) {
            mImageLoader.get(goods, ImageLoader.getCommonViewImageListener(qrCode, R.drawable.defeat, R.drawable.defeat));
        }
        if (!app_pic.equals(null)&&!"".equals(app_pic)) {
            mImageLoader.get(app_pic, ImageLoader.getCommonViewImageListener(qrCode, R.drawable.defeat, R.drawable.defeat));
        }

    }
}
