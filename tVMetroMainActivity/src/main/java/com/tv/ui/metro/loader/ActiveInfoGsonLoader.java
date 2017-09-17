package com.tv.ui.metro.loader;

import android.content.Context;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.httpurl.HttpUrl;
import com.tv.ui.metro.model.ActiveInfoBean;
import com.tv.ui.metro.model.ActiveInfoRespBean;
import com.tv.ui.metro.model.GenericSubjectItem;
import com.tv.ui.metro.view.EncrypePreRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by tv metro on 2/8/2017.
 */
public class ActiveInfoGsonLoader extends BaseMyGsonLoader<ActiveInfoRespBean> {
    public static int LOADER_ID = 0x403;
    @Override
    public void setCacheFileName() {
        cacheFileName = "tabs_activeinfo.cache";
    }


    @Override
    public void setLoaderURL(ActiveInfoRespBean item) {


    }

    public ActiveInfoGsonLoader(Context context, ActiveInfoRespBean respbean) {
        super(context, respbean);
    };

    public ActiveInfoGsonLoader(Context context, ActiveInfoRespBean respbean, Map<String, String> args) {
        super(context, respbean);
        this.args = args ;
    };

    Map<String, String> args ;

    @Override
    protected void loadDataByGson() {
        //========================================================begin
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("type","1");
        hm.put("mac",HttpUrl.getMacAddress());
        hm.put("account","");
        hm.put("versionCode",getVersionCode());

        Iterator<String> it = args == null?null:this.args.keySet().iterator();
        StringBuffer sb = new StringBuffer();
        while(it != null && it.hasNext()){
            String key = it.next();
            hm.put(key, args.get(key));
            sb.append("&");
            sb.append(key);
            sb.append("=");
            sb.append(args.get(key));
        }
        String tokenBak = EncrypePreRequest.encrypePreQuest(hm);
        calledURL = HttpUrl.url_activeinfo+HttpUrl.getMacAddress()+sb.toString()+"&token="+tokenBak+"&versionCode=" + getVersionCode();
        sb = null ;

        //========================================================end

        RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
        GsonRequest<ActiveInfoRespBean> gsonRequest = new GsonRequest<ActiveInfoRespBean>(calledURL, new TypeToken<ActiveInfoRespBean>(){}.getType(), null, listener, errorListener);
        gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName);
        requestQueue.add(gsonRequest);

    }
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
}
