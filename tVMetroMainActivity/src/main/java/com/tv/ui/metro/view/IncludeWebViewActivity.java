package com.tv.ui.metro.view;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tv.ui.metro.R;
import com.tv.ui.metro.galleryviewpager.Gallery2;
import com.tv.ui.metro.utils.JavaScriptinterface;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class IncludeWebViewActivity extends Activity {
    private WebView wv_webview;
    private String str_site;
    private  Intent intent;
    private  String activity_Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_include_web_view);
        Bundle bundle = this.getIntent().getExtras();
        str_site = bundle.getString("site");
        initView();
        initData();


    }
    public void initView() {
        wv_webview = (WebView) findViewById(R.id.webView);
        wv_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

//        //支持App内部javascript交互
//
//        wv_webview.getSettings().setJavaScriptEnabled(true);
//
//        //自适应屏幕
//
//        wv_webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//
//        wv_webview.getSettings().setLoadWithOverviewMode(true);
//
//        //设置可以支持缩放
//
//        wv_webview.getSettings().setSupportZoom(true);
//
//        //扩大比例的缩放
//
        //       wv_webview.getSettings().setUseWideViewPort(true);
//
//        //设置是否出现缩放工具
//
        wv_webview.getSettings().setBuiltInZoomControls(true);
        wv_webview.getSettings().setJavaScriptEnabled(true);
        wv_webview.getSettings().setDefaultTextEncodingName("utf-8");
        wv_webview.setBackgroundColor(Color.parseColor("#FFFFFF"));
        ; // 设置背景色
        wv_webview.getBackground().setAlpha(50); // 设置填充透明度 范围：0-255
        wv_webview.loadDataWithBaseURL(null, "加载中。。", "text/html", "utf-8", null);
        wv_webview.setVisibility(View.VISIBLE); // 加载完之后进行设置显示，以免加载时初始化效果不好看
        wv_webview.addJavascriptInterface(new JavaScriptinterface(this), "test");//AndroidtoJS类对象映射到js的test对象
    }
    public void initData() {
        wv_webview.loadUrl(str_site);
    }

    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == keyEvent.KEYCODE_BACK) {
            if (wv_webview.canGoBack()) {
                wv_webview.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, keyEvent);
    }
    class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }
    }
    public class JavaScriptinterface {
        public   String a;
        private Context mContext;
        //这个一定要定义，要不在showToast()方法里没办法启动intent
        Activity activity;

        /** Instantiate the interface and set the context */
        public JavaScriptinterface(Context c) {
            mContext = c;
            activity = (Activity) c;
        }

        /** 与js交互时用到的方法，在js里直接调用的 */
        public void showToast(String goodsid) {
            a=goodsid;
            Toast.makeText(getApplicationContext(),a,Toast.LENGTH_SHORT).show();
        }
    }

}
