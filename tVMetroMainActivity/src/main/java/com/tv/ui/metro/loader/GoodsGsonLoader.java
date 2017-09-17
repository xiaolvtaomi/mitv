package com.tv.ui.metro.loader;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.httpurl.HttpUrl;
import com.tv.ui.metro.model.ActiveInfoRespBean;
import com.tv.ui.metro.model.GoodsInfoBean;
import com.tv.ui.metro.view.EncrypePreRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Lenovo on 2017/5/11.
 */
public class GoodsGsonLoader extends BaseGoodsLoder<GoodsInfoBean> {
    public static int LOADER_ID = 0x403;
    @Override
    public void setCacheFileName() {
        cacheFileName = "tabs_activeinfo.cache";
    }


    @Override
    public void setLoaderURL(GoodsInfoBean item) {


    }

    public GoodsGsonLoader(Context context,GoodsInfoBean respbean) {
        super(context, respbean);
    };

    public GoodsGsonLoader(Context context, GoodsInfoBean respbean, Map<String, String> args) {
        super(context, respbean);
        this.args = args ;
    };

    Map<String, String> args ;

    @Override
    protected void loadDataByGson() {
        //========================================================begin
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("type","1");
        hm.put("mac", HttpUrl.getMacAddress());
        hm.put("account","");

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

        sb = null ;

        //========================================================end

        RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
        GsonRequest<GoodsInfoBean> gsonRequest = new GsonRequest<GoodsInfoBean>(calledURL, new TypeToken<GoodsInfoBean>(){}.getType(), null, listener, errorListener);
        gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName);
        requestQueue.add(gsonRequest);

    }

}
