package com.tv.ui.metro;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.httpurl.HttpUrl;
import com.tv.ui.metro.loader.BaseGsonLoader;
import com.tv.ui.metro.loader.TabsGsonLoader;
import com.tv.ui.metro.menu.MainMenuMgr;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericSubjectItem;
import com.tv.ui.metro.model.ImageGroup;
import com.tv.ui.metro.model.TopBar;
import com.tv.ui.metro.utils.DBUtils;
import com.tv.ui.metro.utils.ViewUtils;
import com.tv.ui.metro.view.*;


public class MainActivity extends FragmentActivity implements MainMenuMgr
        .OnMenuCancelListener, LoaderManager
        .LoaderCallbacks<GenericSubjectItem<DisplayItem>> {
    private final static String TAG = "TVMetro-MainActivity";
    protected BaseGsonLoader mLoader;
    String line;
    TabHost mTabHost;
    TabWidget mTabs;
    ViewPager mViewPager;
    TabsAdapter mTabsAdapter;
    TitleView titlebar;
    boolean isFromRoot;
    EmptyLoadingView mLoadingView;
    FrameLayout main_bg;
    GenericSubjectItem<DisplayItem> _contents;
    boolean mTabChanging;
    int mPrePagerPosition = 0;
    protected DisplayItem albumItem;
    public String panelgroupid = null;
    private ImageLoader mImageLoader;
    private String panelgroupids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = this.getIntent().getExtras();
        //不加bundle非空判断，第一次启动，bundle报空指针
        panelgroupids=getIntent().getStringExtra("panelgroupid");
        if (panelgroupids!=null){
            panelgroupid = panelgroupids;
        }
        if (bundle != null) {
            panelgroupid = bundle.getString("panelgroupid");
        }

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabs = (TabWidget) findViewById(android.R.id.tabs);
        ViewStub vStub = (ViewStub) findViewById(R.id.new_home_menu);
        mMenuContainer = (FrameLayout) vStub.inflate();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        titlebar = (TitleView) findViewById(R.id.titleview);
        main_bg = (FrameLayout) findViewById(R.id.main_tabs_container);
        mImageLoader = VolleyHelper.getInstance(getApplicationContext()
                .getApplicationContext()).getImageLoader();
        mLoadingView = makeEmptyLoadingView(this, (RelativeLayout)
                findViewById(R.id.tabs_content));
        setScrollerTime(800);
        albumItem = (DisplayItem) getIntent().getSerializableExtra("item");
        getSupportLoaderManager().initLoader(TabsGsonLoader.LOADER_ID, null,
                this);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }

    }

    public void bg() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewUtils.unbindDrawables(findViewById(R.id.main_tabs_container));


    }

    //please override this fun

    protected void createTabsLoader() {
        mLoader = new TabsGsonLoader(this, albumItem);
    }

    @Override
    public Loader<GenericSubjectItem<DisplayItem>> onCreateLoader(int loaderId, Bundle bundle) {
        if (loaderId == TabsGsonLoader.LOADER_ID) {
            createTabsLoader();
            mLoader.setProgressNotifiable(mLoadingView);
            return mLoader;
        } else {
            return null;
        }
    }


    final static String buildInData2 = "\n" +
            "\n" +
            "{\"code\":\"000\",\"update_time\": 1505295244202,\"data\": " +
            "[{\"name\": \"第一页\",\"panelbg\": \"\",\"wc\": 8,\"hc\": 6," +
            "\"items\": [{\"target\": {\"jump_uri\":\"http://10.254.203" +
            ".196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":1,\"x\":1}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"舞钢市\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/1505182214873cfd728bf-4655" +
            "-4498-9007-f76cd3c15c13.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"461\",\"name\": \"舞钢市\"," +
            "\"focusable\": \"1\"},{\"target\": {\"jump_uri\":\"http://10.254" +
            ".203.196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":1,\"x\":3}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"宝丰县\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/15051823607699a7dfc72-8e79" +
            "-409e-8de9-dc865ea2e2fb.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"462\",\"name\": \"宝丰县\"," +
            "\"focusable\": \"1\"},{\"target\": {\"jump_uri\":\"http://10.254" +
            ".203.196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":1,\"x\":5}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"郏县\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/1505182392626114f3b5d-f516" +
            "-41f5-a845-c9243ef86ec2.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"463\",\"name\": \"郏县\"," +
            "\"focusable\": \"1\"},{\"target\": {\"jump_uri\":\"http://10.254" +
            ".203.196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":1,\"x\":7}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"鲁山县\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/15051824139589a5fcb31-e0e9" +
            "-49ab-8933-a61ef13bc4d3.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"464\",\"name\": \"鲁山县\"," +
            "\"focusable\": \"1\"},{\"target\": {\"jump_uri\":\"http://10.254" +
            ".203.196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":4,\"x\":1}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"叶县\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/1505182446420cb466339-ba24" +
            "-499b-802c-39cee9e9705c.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"465\",\"name\": \"叶县\"," +
            "\"focusable\": \"1\"},{\"target\": {\"jump_uri\":\"http://10.254" +
            ".203.196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":4,\"x\":3}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"新华区\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/1505182469101b9825418-2d42" +
            "-4511-bc5e-da56833e4a76.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"466\",\"name\": \"新华区\"," +
            "\"focusable\": \"1\"},{\"target\": {\"jump_uri\":\"http://10.254" +
            ".203.196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":4,\"x\":5}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"卫东区\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/1505182492785dcd72091-b5e9" +
            "-4462-98e0-8ce41af15c7f.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"467\",\"name\": \"卫东区\"," +
            "\"focusable\": \"1\"},{\"target\": {\"jump_uri\":\"http://10.254" +
            ".203.196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":4,\"x\":7}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"湛河区\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/1505182516707548a7077-d9c8" +
            "-400c-9e8f-7eac86502e9d.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"468\",\"name\": \"湛河区\"," +
            "\"focusable\": \"1\"}],\"_ui\": {\"type\": \"metro\"},\"ns\": " +
            "\"app\",\"type\": \"board\",\"id\": \"82\"},{\"name\": \"第二页\"," +
            "\"panelbg\": \"\",\"wc\": 8,\"hc\": 6,\"items\": [{\"target\": " +
            "{\"jump_uri\":\"http://10.254.203" +
            ".196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":1,\"x\":1}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"石龙区\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/15051825452150194173d-58ad" +
            "-4aa3-90c5-4d52fb7b67bf.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"469\",\"name\": \"石龙区\"," +
            "\"focusable\": \"1\"},{\"target\": {\"jump_uri\":\"http://10.254" +
            ".203.196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":1,\"x\":3}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"新城区\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/1505182608829cf8ab679-bd4a" +
            "-4cb8-82c2-cc6e821dc2b0.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"470\",\"name\": \"新城区\"," +
            "\"focusable\": \"1\"},{\"target\": {\"jump_uri\":\"http://10.254" +
            ".203.196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":1,\"x\":5}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"高新区\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/1505182629209ab491b97-720b" +
            "-4c7c-ae40-2dc798aa538d.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"471\",\"name\": \"高新区\"," +
            "\"focusable\": \"1\"},{\"target\": {\"jump_uri\":\"http://10.254" +
            ".203.196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":1,\"x\":7}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"储备\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/1505182695116a10c4a8a-aac3" +
            "-4caf-9d45-019a07cc8e74.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"472\",\"name\": \"储备\"," +
            "\"focusable\": \"1\"},{\"target\": {\"jump_uri\":\"http://10.254" +
            ".203.196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":4,\"x\":1}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"储备\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/1505182721681e0e78b79-e8dc" +
            "-4224-b75f-e3d7dd8d0efd.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"473\",\"name\": \"储备\"," +
            "\"focusable\": \"1\"},{\"target\": {\"jump_uri\":\"http://10.254" +
            ".203.196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":4,\"x\":3}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"储备\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/1505182741112f817d420-be40" +
            "-4bc9-94f9-b6ab43685fc4.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"474\",\"name\": \"储备\"," +
            "\"focusable\": \"1\"},{\"target\": {\"jump_uri\":\"http://10.254" +
            ".203.196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":4,\"x\":5}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"储备\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/1505182760011045563f1-0144" +
            "-48e5-9d33-39f4d5d80b08.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"475\",\"name\": \"储备\"," +
            "\"focusable\": \"1\"},{\"target\": {\"jump_uri\":\"http://10.254" +
            ".203.196:8081/panda/cms/noticeListByMac\",\"param\":\"\"," +
            "\"subType\":\"single\",\"type\":\"open_url\"}," +
            "\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"w\":2," +
            "\"h\":3,\"y\":4,\"x\":7}},\"superscript\": \"\",\"images\": " +
            "{\"text\":{\"ani\":{},\"url\":\"储备\",\"pos\":{}}," +
            "\"icon\":{\"ani\":{},\"url\":\"\",\"pos\":{}}," +
            "\"back\":{\"ani\":{},\"url\":\"http://10.254.203" +
            ".196:8081/panda/cms/images/2017/09/12/1505182792140f2685619-ca5f" +
            "-408b-b079-5b9e93166953.png\",\"pos\":{}}," +
            "\"spirit\":{\"ani\":{},\"url\":\"\",\"pos\":{}}},\"type\": " +
            "\"album\",\"ns\": \"app\",\"id\": \"476\",\"name\": \"储备\"," +
            "\"focusable\": \"1\"}],\"_ui\": {\"type\": \"metro\"},\"ns\": " +
            "\"app\",\"type\": \"board\",\"id\": \"83\"}]}";


    @Override
    public void onLoadFinished(Loader<GenericSubjectItem<DisplayItem>>
                                           tabsLoader, final
    GenericSubjectItem<DisplayItem> tabs) {
        if (tabs != null && tabs.data != null && tabs.data.size() > 0) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    updateTabsAndMetroUI(tabs);
                    mTabHost.requestLayout();
                }
            });
        } else {
            if (panelgroupid == null) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //this is the code for test
                        mLoadingView.stopLoading(true, false);
                        InputStream inputStream = null;
                        try {
                            inputStream = getAssets().open("json/home.txt");
                            int size = inputStream.available();
                            int len = -1;
                            byte[] bytes = new byte[size];
                            inputStream.read(bytes);
                            inputStream.close();
                            line = new String(bytes);
                            Log.d("aa", line); }
                        catch (IOException e) {
                            e.printStackTrace(); }
                        System.out.println(line);
                        Gson gson = new Gson();
                        GenericSubjectItem<DisplayItem> fromJson = gson
                                .fromJson(line, new
                                        TypeToken<GenericSubjectItem<DisplayItem>>() {
                                        }.getType());
                        updateTabsAndMetroUI(fromJson);
                        mTabHost.requestLayout();
                        final View tabView = mTabs.getChildTabViewAt
                                (mViewPager.getCurrentItem());
                        tabView.post(new Runnable() {
                            @Override
                            public void run() {
                                tabView.requestFocus();
                            }
                        });

                    }
                });
            } else if (panelgroupid != null && panelgroupid.equals("50")) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //this is the code for test
                        mLoadingView.stopLoading(true, false);
                        //load test code for out of companny
                        Gson gson = new Gson();
                        GenericSubjectItem<DisplayItem> fromJson = gson
 .fromJson(buildInData2, new TypeToken<GenericSubjectItem<DisplayItem>>() {
                        }.getType());

                        updateTabsAndMetroUI(fromJson);
                        mTabHost.requestLayout();
                        final View tabView = mTabs.getChildTabViewAt
                                (mViewPager.getCurrentItem());
                        tabView.post(new Runnable() {
                            @Override
                            public void run() {
                                tabView.requestFocus();
                            }
                        });

                    }
                });
            } else if (panelgroupid != null && panelgroupid.equals("51")) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //this is the code for test
                        mLoadingView.stopLoading(true, false);

                        GenericSubjectItem<DisplayItem> fromJson = DBUtils
                                .getDisplayItemsByGroupId(null, "medical");
                        updateTabsAndMetroUI(fromJson);


                        updateTabsAndMetroUI(fromJson);
                        mTabHost.requestLayout();
                        final View tabView = mTabs.getChildTabViewAt
                                (mViewPager.getCurrentItem());
                        tabView.post(new Runnable() {
                            @Override
                            public void run() {
                                tabView.requestFocus();
                            }
                        });

                    }
                });
            } else if (panelgroupid != null && panelgroupid.equals("59")) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //this is the code for test
                        mLoadingView.stopLoading(true, false);

                        GenericSubjectItem<DisplayItem> fromJson = DBUtils
                                .getDisplayItemsByGroupId(null, "law");
                        updateTabsAndMetroUI(fromJson);


                        updateTabsAndMetroUI(fromJson);
                        mTabHost.requestLayout();
                        final View tabView = mTabs.getChildTabViewAt
                                (mViewPager.getCurrentItem());
                        tabView.post(new Runnable() {
                            @Override
                            public void run() {
                                tabView.requestFocus();
                            }
                        });

                    }
                });
            } else if (panelgroupid != null && panelgroupid.equals("58")) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //this is the code for test
                        mLoadingView.stopLoading(true, false);

                        GenericSubjectItem<DisplayItem> fromJson = DBUtils
                                .getDisplayItemsByGroupId(null, "agriculture");
                        updateTabsAndMetroUI(fromJson);

                        updateTabsAndMetroUI(fromJson);
                        mTabHost.requestLayout();
                        final View tabView = mTabs.getChildTabViewAt
                                (mViewPager.getCurrentItem());
                        tabView.post(new Runnable() {
                            @Override
                            public void run() {
                                tabView.requestFocus();
                            }
                        });

                    }
                });
            } else if(panelgroupid != null && panelgroupid.equals("55")){
                mLoadingView.stopLoading(true, false);
                startActivity(new Intent(MainActivity.this, LoginStep1Activity.class));

            } else if(panelgroupid != null && panelgroupid.equals("56")){
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //this is the code for test
                        mLoadingView.stopLoading(true, false);

                        GenericSubjectItem<DisplayItem> fromJson = DBUtils
                                .getDisplayItemsByGroupId("402", null);
                        updateTabsAndMetroUI(fromJson);

                        updateTabsAndMetroUI(fromJson);
                        mTabHost.requestLayout();
                        final View tabView = mTabs.getChildTabViewAt
                                (mViewPager.getCurrentItem());
                        tabView.post(new Runnable() {
                            @Override
                            public void run() {
                                tabView.requestFocus();
                            }
                        });

                    }
                });
            }
            else if(panelgroupid != null ){
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //this is the code for test
                        mLoadingView.stopLoading(true, false);

                        GenericSubjectItem<DisplayItem> fromJson = DBUtils
                                .getDisplayItemsByGroupId(panelgroupid, null);
                        updateTabsAndMetroUI(fromJson);

                        updateTabsAndMetroUI(fromJson);
                        mTabHost.requestLayout();
                        final View tabView = mTabs.getChildTabViewAt
                                (mViewPager.getCurrentItem());
                        tabView.post(new Runnable() {
                            @Override
                            public void run() {
                                tabView.requestFocus();
                            }
                        });

                    }
                });
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<GenericSubjectItem<DisplayItem>>
                                          tabsLoader) {

    }

    protected void addVideoTestData(GenericSubjectItem<DisplayItem> _content) {
        Log.d(TAG, "addVideoTestData");
    }

    protected void updateTabsAndMetroUI(GenericSubjectItem<DisplayItem>
                                                content) {
        if (_contents != null) {
            if (_contents.update_time == content.update_time) {
                Log.d(TAG, "same content, no need to update UI");
                return;
            }
        }
        mTabs.removeAllViews();
        mViewPager.removeAllViews();
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

        addVideoTestData(content);
        updateTopbar(content.topbar);

        _contents = content;


        for (int i = 0; i < content.data.size(); i++) {
            Bundle args = new Bundle();
            args.putSerializable("tab", content.data.get(i));
            args.putInt("hc", content.data.get(i).hc);
            args.putInt("wc", content.data.get(i).wc);
            args.putString("panelbg", content.data.get(i).panelbg);
            args.putInt("index", i);
            args.putInt("tab_count", content.data.size() + 1);
            if (content.data.get(i).type.equals("board")) {
                mTabsAdapter.addTab(
                        mTabHost.newTabSpec(content.data.get(i).name)
                                .setIndicator(newTabIndicator(content.data
                                        .get(i).name, i == 0)),
                        BoardFragment.class, args);
            } else {
                mTabsAdapter.addTab(
                        mTabHost.newTabSpec(content.data.get(i).name)
                                .setIndicator(newTabIndicator(content.data
                                        .get(i).name, i == 0)),
                        MetroFragment.class, args);
            }

        }


    }

    public void updateTopbar(TopBar topBar) {
        if (topBar == null) {
            return;
        } else {
            titlebar.setTitleBar(topBar.title, topBar.logo);

            SharedPreferences mSharedPreferences = getSharedPreferences
                    ("loginUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("user_bg", topBar.welcomebg);
            editor.commit();
        }


    }


    private FixedSpeedScroller scroller = null;

    public void setScrollerTime(int scrollerTime) {
        try {
            if (scroller != null) {
                scroller.setTime(scrollerTime);
            } else {
                Field mScroller;
                mScroller = ViewPager.class.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                scroller = new FixedSpeedScroller(mViewPager.getContext(),
                        new DecelerateInterpolator());
                scroller.setTime(scrollerTime);
                mScroller.set(mViewPager, scroller);
            }
        } catch (Exception e) {
        }
    }

    private View newTabIndicator(String tabName, boolean focused) {
        final String name = tabName;
        View viewC = View.inflate(this, R.layout.tab_view_indicator_item, null);

        TextViewWithTTF view = (TextViewWithTTF) viewC.findViewById(R.id
                .tv_tab_indicator);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup
                .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams
                .WRAP_CONTENT);
        view.setLayoutParams(lp);

        mTabs.setPadding(getResources().getDimensionPixelSize(R.dimen
                .tab_left_offset), 0, 0, 0);

        view.setText(name);

        if (focused == true) {
            Resources res = getResources();
            view.setTextColor(res.getColor(android.R.color.white));
            view.setTypeface(null, Typeface.BOLD);
            view.requestFocus();
        }
        return viewC;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        showStatusBar(this, true);
        if (mMenuReceiver == null) {
            mMenuReceiver = new MenuReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.tv.ui.metro.action.SEARCH");
            registerReceiver(mMenuReceiver, filter);
        }
    }

    protected void showStatusBar(Context context, boolean isShow) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        showStatusBar(this, false);
        if (mMenuReceiver != null) {
            unregisterReceiver(mMenuReceiver);
            mMenuReceiver = null;
        }
    }

    private MainMenuMgr mMainMenu;
    private FrameLayout mMenuContainer;
    private MenuReceiver mMenuReceiver;
    private boolean mIsTabFocusedShowMenu = false;

    private void showActionMenu(FrameLayout container) {
        if (mMainMenu == null) {
            mMainMenu = new MainMenuMgr(getApplicationContext(), container,
                    this);
        }
        mMainMenu.showMenu(container);
    }

    private boolean isContentMoveLeft = false;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        isContentMoveLeft = false;
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode()
                == KeyEvent.KEYCODE_MENU) {
            Utils.playKeySound(mTabs, Utils.SOUND_KEYSTONE_KEY);
            showActionMenu(mMenuContainer);
            return true;
        }

        //
        //fix for one bug for up key and change the tab
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode()
                == KeyEvent.KEYCODE_DPAD_UP) {

//            View view = this.getCurrentFocus();
//            Object obj = view.getTag(R.integer.tag_view_postion);
//            if(obj != null){
//                int position = (Integer)obj;
//                if(position == 0){
//                    mTabHost.setCurrentTab(mViewPager.getCurrentItem());
//
//                    Utils.playKeySound(mTabs, Utils.SOUND_KEYSTONE_KEY);
//                    //set highlight
//                    final View tabView = mTabs.getChildTabViewAt(mViewPager
// .getCurrentItem());
//                    tabView.post(new Runnable(){
//                        @Override
//                        public void run() {
//                            tabView.requestFocus();
//                        }
//                    });
//
//                    return true;
//                }
//            }


            View view = this.getCurrentFocus();
            if (view.getId() == R.id.tv_tab_indicator && mViewPager
                    .getCurrentItem() != mViewPager.getChildCount() - 1) {
                BaseScrollLisener fragment = (BaseScrollLisener) mTabsAdapter
                        .getItem(mViewPager.getCurrentItem());
                fragment.focusMoveToUp();
                return true;
            }

            if (view.getId() == R.id.tv_tab_indicator && mViewPager
                    .getCurrentItem() == mViewPager.getChildCount() - 1) {
                BaseScrollLisener fragment = (BaseScrollLisener) mTabsAdapter
                        .getItem(mViewPager.getCurrentItem());
                fragment.focusMoveToUp();
                return true;
            }
        }

        View view = getCurrentFocus();
        isContentMoveLeft = (
                event.getAction() == KeyEvent.ACTION_UP
                        && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT
                        && TextViewWithTTF.class.isInstance(view) == false);

        if (
                event.getAction() == KeyEvent.ACTION_DOWN
                        && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT
                        || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT)
                        && TextViewWithTTF.class.isInstance(view) == true) {

            //already in left or right, no need do focus move
            if ((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT &&
                    mViewPager.getCurrentItem() == 0) || (event.getKeyCode()
                    == KeyEvent.KEYCODE_DPAD_RIGHT && mViewPager
                    .getCurrentItem() == mViewPager.getChildCount() - 1)) {

                Utils.playKeySound(mTabs, Utils.SOUND_ERROR_KEY);
                return true;
            }
            setScrollerTime(500);
        } else {
            setScrollerTime(500);
        }

        if (
                event.getAction() == KeyEvent.ACTION_DOWN
                        && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN)
                        && TextViewWithTTF.class.isInstance(view) == true) {
            //already in left or right, no need do focus move
            if ((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN)) {
                Utils.playKeySound(mTabs, Utils.SOUND_ERROR_KEY);
                return true;
            }
            setScrollerTime(500);
        } else {
            setScrollerTime(500);
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && (event.getKeyCode()
                == KeyEvent.KEYCODE_DPAD_DOWN || event.getKeyCode() ==
                KeyEvent.KEYCODE_ENTER)) {
//            view = this.getCurrentFocus();
//            if(view.getId() == R.id.tv_tab_indicator){
//                BaseScrollLisener fragment = (BaseScrollLisener)
// mTabsAdapter.getItem(mViewPager.getCurrentItem());
//                fragment.focusMoveToLeft();
//                return true;
//            }

            view = this.getCurrentFocus();
            if (view != null) {
                Object obj = view.getTag(R.integer.tag_view_postion);

                if (obj != null) {
                    int position = (Integer) obj;
                    int currentview_hc = 3;
                    {
                        // fuzhi currentview_hc
                        if (BoardFragment.class.isInstance(mTabsAdapter
                                .getItem(mViewPager.getCurrentItem()))) {
                            currentview_hc = ((BoardFragment) mTabsAdapter
                                    .getItem(mViewPager.getCurrentItem()))
                                    .getHc();
                        }
                    }

                    if (position == currentview_hc + 1) {
                        mTabHost.setCurrentTab(mViewPager.getCurrentItem());

                        Utils.playKeySound(mTabs, Utils.SOUND_KEYSTONE_KEY);
                        //set highlight
                        final View tabView = mTabs.getChildTabViewAt
                                (mViewPager.getCurrentItem());
                        tabView.post(new Runnable() {
                            @Override
                            public void run() {
                                tabView.requestFocus();
                            }
                        });

                        return true;
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onMenuCancel() {
        if (mIsTabFocusedShowMenu) {
            //mNavBar.backToLastFocusTabView();
            mIsTabFocusedShowMenu = false;
        }
    }

    protected String dataSchemaForSearchString =
            "misearch://applicationsearch/";

    protected void setSeachSchema(String schema) {
        dataSchemaForSearchString = schema;
    }

    public class MenuReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context content, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.tv.ui.metro.action.SEARCH")) {
                try {
                    Intent searchIntent = new Intent(Intent.ACTION_VIEW);
                    searchIntent.setData(Uri.parse(dataSchemaForSearchString));
                    startActivity(searchIntent);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }


    public class TabsAdapter extends FragmentPagerAdapter
            implements TabHost.OnTabChangeListener, ViewPager
            .OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
        private final FragmentManager fm;

        final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity, TabHost tabHost,
                           ViewPager pager) {
            super(activity.getSupportFragmentManager());
            fm = activity.getSupportFragmentManager();
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle
                args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();
            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object
                object) {
            //container.removeView(fragments.get(new Integer(position))
            // .getView());
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = this.getItem(position);

            if (!fragment.isAdded()) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(fragment, fragment.getClass().getSimpleName());

                ft.commit();

                fm.executePendingTransactions();
            }

            if (fragment.getView() != null && fragment.getView().getParent()
                    == null) {
                container.addView(fragment.getView());
            }

            return fragment;
        }

        HashMap<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();

        @Override
        public Fragment getItem(int position) {
            Fragment fg = fragments.get(new Integer(position));
            if (fg == null) {
                TabInfo info = mTabs.get(position);
                fg = Fragment.instantiate(mContext, info.clss.getName(), info
                        .args);
                fragments.put(new Integer(position), fg);
            }
            return fg;
        }

        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mTabChanging = true;
            mViewPager.setCurrentItem(position);
            mTabChanging = false;
            switchTabView(position);
            if (position < _contents.data.size()) {
                ImageGroup ig = _contents.data.get(position).images;
                if (ig != null) {
                    if (ig.back() != null && ig.back().url != null) {
                        //VolleyHelper.getInstance(MainActivity.this)
                        // .getImageLoader().get(ig.back.url, ImageLoader
                        // .getCommonViewImageListener(findViewById(R.id
                        // .main_tabs_container), 0, 0));
                    }
                }
            }
        }

        private void switchTabView(int index) {
            switchTab(index);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int
                positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
            if (!mTabChanging) {
                if (position < mPrePagerPosition) {
                    BaseScrollLisener mf = (BaseScrollLisener) fragments.get
                            (new Integer(position));
                    mf.focusMoveToRight();
                } else if (position > mPrePagerPosition) {
                    BaseScrollLisener mf = (BaseScrollLisener) fragments.get
                            (new Integer(position));
                    mf.focusMoveToLeft();
                }
            } else {
                if (position < mPrePagerPosition) {
                    BaseScrollLisener mf = (BaseScrollLisener) fragments.get
                            (new Integer(position));
                    mf.scrollToLeft(true);
                    BaseScrollLisener premf = (BaseScrollLisener) fragments
                            .get(new Integer(mPrePagerPosition));
                    premf.scrollToLeft(false);
                } else if (position > mPrePagerPosition) {
                    BaseScrollLisener mf = (BaseScrollLisener) fragments.get
                            (new Integer(position));
                    mf.scrollToLeft(false);
                    BaseScrollLisener premf = (BaseScrollLisener) fragments
                            .get(new Integer(mPrePagerPosition));
                    premf.scrollToRight(false);
                }
            }
            mPrePagerPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    public void switchTab(int index) {
        TabWidget tw = mTabHost.getTabWidget();
        for (int i = 0; i < tw.getChildCount(); i++) {
            View viewC = tw.getChildTabViewAt(i);
            //Log.d(TAG, "tab width="+viewC.getWidth() + " left="+viewC
            // .getLeft());
            if (i == index) {
                TextViewWithTTF view = (TextViewWithTTF) viewC.findViewById(R
                        .id.tv_tab_indicator);
                Resources res = view.getResources();
                view.setTextColor(res.getColor(android.R.color.white));
                view.setTypeface(null, Typeface.BOLD);
                if (_contents.data.get(i).panelbg != null) {
                    mImageLoader.get(_contents.data.get(i).panelbg,
                            ImageLoader.getCommonViewImageListener(main_bg, R
                                    .drawable.main_bg, R.drawable.main_bg));
                } else {
                    main_bg.setBackgroundResource(R.drawable.main_bg);
                }
            } else {
                TextViewWithTTF view = (TextViewWithTTF) viewC.findViewById(R.id.tv_tab_indicator);
                Resources res = view.getResources();
                view.setTextColor(res.getColor(R.color.white_50));
                view.setTypeface(null, Typeface.NORMAL);
            }
        }
    }

    public static EmptyLoadingView makeEmptyLoadingView(Context context, RelativeLayout parentView) {
        return makeEmptyLoadingView(context, parentView, RelativeLayout.CENTER_IN_PARENT);
    }

    public static EmptyLoadingView makeEmptyLoadingView(Context context, RelativeLayout parentView, int rule) {
        EmptyLoadingView loadingView = new EmptyLoadingView(context);
        loadingView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rlp.addRule(rule);
        parentView.addView(loadingView, rlp);
        return loadingView;
    }

}
