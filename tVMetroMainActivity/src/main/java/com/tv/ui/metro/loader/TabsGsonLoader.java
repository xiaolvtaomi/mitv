package com.tv.ui.metro.loader;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.R;
import com.tv.ui.metro.httpurl.HttpUrl;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericSubjectItem;
import com.tv.ui.metro.view.EncrypePreRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class TabsGsonLoader extends BaseGsonLoader<GenericSubjectItem<DisplayItem>> {
    public static int LOADER_ID = 0x401;
    private String is;
    @Override
    public void setCacheFileName(DisplayItem item) {
        if(item == null) {
            cacheFileName = "tabs_content2.cache";
        }else{
            cacheFileName = "tabs_content_"+item.target.jump_uri+".cache";
        }
    }

    @Override
    public void setLoaderURL(DisplayItem item) {
//      calledURL = "http://appstore.duokanbox.com/v2/app/home";
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("type","1");
        hm.put("mac",HttpUrl.getMacAddress());
        hm.put("account","");
        hm.put("versionCode",getVersionCode());
        // 特殊处理
        String pannelgroupid = "";
        if(item != null && item.target != null && !TextUtils.isEmpty(item.target.type)
                && "open_group".equals(item.target.type)){
            pannelgroupid = item.target.jump_uri ;
        }


        if (TextUtils.isEmpty(pannelgroupid)) {
            String tokenBak = EncrypePreRequest.encrypePreQuest(hm);
            //  calledURL = HttpUrl.calledurl + HttpUrl.getMacAddress() +
            // "&token=" + tokenBak;
            // calledURL = HttpUrl.calledurl + HttpUrl.getMacAddress() +
            // "&token=" + tokenBak+"&versionCode=" + getVersionCode();


        } else {
            hm.put("panelgroupid", pannelgroupid);
            String tokenBak = EncrypePreRequest.encrypePreQuest(hm);
            //       calledURL = HttpUrl.calledurl + HttpUrl.getMacAddress()
            // + "&token=" + tokenBak+ "&panelgroupid="+pannelgroupid;
            //       calledURL = "file:///android_asset/json";
            //      calledURL = HttpUrl.calledurl + HttpUrl.getMacAddress() +
            // "&token=" + tokenBak+
            // "&panelgroupid="+pannelgroupid+"&versionCode=" +
            // getVersionCode();

        }
//        // 特殊处理
//        String goodsgroupid = "";
//        if(item != null && item.target != null && !TextUtils.isEmpty(item
// .target.type)
//                && "open_group".equals(item.target.type)){
//            goodsgroupid = item.target.jump_uri ;
//        }
//
//
//        if (TextUtils.isEmpty(goodsgroupid)) {
//            String tokenBak = EncrypePreRequest.encrypePreQuest(hm);
//            calledURL = HttpUrl.goodsgroup_calledurl + HttpUrl.getMacAddress() ;
//        }
//        else {
//            hm.put("goodsgroupid",goodsgroupid);
//            String tokenBak = EncrypePreRequest.encrypePreQuest(hm);
//            calledURL = HttpUrl.goodsgroup_calledurl + HttpUrl.getMacAddress()+ "&groupId="+goodsgroupid;
//        }
    }
    // 获取版本号
    public int getVersionCode()
    {
        try {
        // 获取packagemanager的实例
        PackageManager manager = getContext().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);
        int version = info.versionCode;
        return version;
        }  catch (Exception e) {
        e.printStackTrace();
        return 0;
    }
    }
    public TabsGsonLoader(Context context, DisplayItem item) {
        super(context, item);
    };
    @Override
    protected void loadDataByGson() {
        RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
        GsonRequest<GenericSubjectItem<DisplayItem>> gsonRequest = new GsonRequest<GenericSubjectItem<DisplayItem>>(calledURL, new TypeToken<GenericSubjectItem<DisplayItem>>(){}.getType(), null, listener, errorListener);
        gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName);
        requestQueue.add(gsonRequest);
    }
}
