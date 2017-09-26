package com.tv.ui.metro.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tv.ui.metro.R;
import com.tv.ui.metro.loader.ActiveInfoGsonLoader;
import com.tv.ui.metro.model.ActiveInfoRespBean;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericAlbum;
import com.tv.ui.metro.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 只有一页数据,5*3数据
 */
public class BoardFragment extends Fragment implements BaseScrollLisener,LoaderManager.LoaderCallbacks<ActiveInfoRespBean>{
    private final String TAG = "BoardFragment";
    public BoardLayout mBoardLayout;
    public   GenericAlbum<DisplayItem> tab;
    //行数
    int wc=0 ;
    int hc=0 ;
    // 面板向的内容监听
    Map<String, ActiveInfoLisener> listeners = new HashMap<String, ActiveInfoLisener>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = null;
        Log.d(TAG, "onCreateView =" + this);
        v = inflater.inflate(R.layout.boardfragment, container, false);
        mBoardLayout = (BoardLayout) v.findViewById(R.id.boardlayout);
//  mBoardLayout.setMetroCursorView((MetroCursorView) v.findViewById(R.id.metrocursor));
        tab = (GenericAlbum) this.getArguments().getSerializable("tab");
        if ( this.getArguments().getInt("wc")>0){
            wc = this.getArguments().getInt("wc") ;
        }
        if ( this.getArguments().getInt("hc")>0){
            hc = this.getArguments().getInt("hc") ;
        }
        mBoardLayout.setShape(wc,hc);

        initViews();
//        initData();

        return v;
    }

    //请求活动接口
    public void initData(){
        //  网络请求和解析
        getLoaderManager().initLoader(ActiveInfoGsonLoader.LOADER_ID, null, this);
    }

    public View addView(View view, int x, int y , int wc, int hc,String focusable){
        return mBoardLayout.addItemView(view, x,y,wc,hc,focusable);
    }

    public void setWcHc(int wc, int hc){
        this.wc = wc ;
        this.hc = hc ;
    }

    public int getWc(){
        return wc ;
    }
    public int getHc(){
        return hc ;
    }

    public View addView(View view, int x, int y , int wc, int hc, int padding,String focusable){
        return mBoardLayout.addItemView(view, x,y,wc,hc, padding,focusable);
    }

    public ArrayList<View> createUserView(){
        return UserViewFactory.getInstance().createUserView(getActivity());
    }

    public void initViews(){


        if(tab != null && tab.items != null){
            boolean needNetRequest = false ;
            Collections.sort(tab.items);

            StringBuffer sb_ids = new StringBuffer();
            sb_ids.append("-100"); // 服务器这样屏蔽最后是逗号的情况
            for(DisplayItem item:tab.items){
                int type = MetroLayout.Normal;

                if(item._ui.layout.w == item._ui.layout.h){
                    type = MetroLayout.Normal;
                }else if(item._ui.layout.w > item._ui.layout.h){
                    type = MetroLayout.Horizontal;
                }else if(item._ui.layout.w < item._ui.layout.h){
                    type = MetroLayout.Vertical;

                }
                // 给RecommendCardView注册监听 ，监听着内容刷新
                RecommendCardView tempview = new RecommendCardView(getActivity(), type).bindData(item);

                addView(tempview,
                        item._ui.layout.x,
                        item._ui.layout.y,
                        item._ui.layout.w,
                        item._ui.layout.h,
                        item.focusable
                );

                if(!TextUtils.isEmpty(item.type)&&item.type.equals("news")){
                    needNetRequest = true ;
                    sb_ids.append(",");
                    sb_ids.append(item.id);
//                    listeners.put(item.ns, tempview);
                    listeners.put(item.id, tempview);
                }

            }

            //
            args.put("id", sb_ids.toString());
            sb_ids = null ;

            // 如果界面的面板项有不是默认的ns，就说明这个界面有面板项要请求接口返回数据来更新内容
            if(needNetRequest){
                initData();
            }


        }
    }

    private View   lastPostionView;
    public View getLastPositionView(){
        return lastPostionView;
    }
    public void focusMoveToUp(){
        mBoardLayout.focusMoveToUp();
    }
    public void focusMoveToLeft(){
        mBoardLayout.focusMoveToLeft();
    }

    public void focusMoveToRight(){
        mBoardLayout.focusMoveToRight();
    }

    public void focusMoveToPreFocused(){
        mBoardLayout.focusMoveToPreFocused();
    }


    @Override
    public void scrollToLeft(boolean fullscroll) {

    }

    @Override
    public void scrollToRight(boolean fullscroll) {

    }
    ActiveInfoGsonLoader mLoader ;
    ActiveInfoRespBean respdata ;
    Map<String , String> args = new HashMap<String, String>() ;
    //please override this fun
    protected void createTabsLoader(){
        if(getActivity() != null) {
            mLoader = new ActiveInfoGsonLoader(getActivity(), respdata, args);
        }
    }

    @Override
    public Loader<ActiveInfoRespBean> onCreateLoader(int loaderId, Bundle args) {
        if(loaderId == ActiveInfoGsonLoader.LOADER_ID){
            createTabsLoader();
//            mLoader.setProgressNotifiable(mLoadingView);
            return mLoader;
        }else{
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<ActiveInfoRespBean> loader, ActiveInfoRespBean data) {
        respdata = data ;

            if (respdata!=null && respdata.getData() != null && respdata.getData().size() > 0) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        updateCommedViewInfo();
                    }
                });
            }

    }

    // 更新界面：回掉lisnter
    public void updateCommedViewInfo(){
        Iterator<String> it = listeners.keySet().iterator();
        if (respdata.getData().get("notice") != null && respdata.getData().get("notice").size() > 0) {

            while (respdata.getData() != null && it.hasNext()) {
                String key_itemid = it.next();
                for (int i = 0; i < respdata.getData().get("notice").size() ;i++){
                    String temp = respdata.getData().get("notice").get(i).itemsId;
                    if(!TextUtils.isEmpty(temp) && key_itemid.equals(temp)){
                        listeners.get(temp).activeInfoChanged(respdata.getData().get("notice").get(i));
                        break;
                    }
                }
            }

        }
//        while (respdata.getData() != null && it.hasNext()) {
//            String key_itemid = it.next();
//            if (respdata.getData().get("notice") != null && respdata.getData().get("notice").size() > 0) {
//                for (int i = 0 ;i)
//                listeners.get(key).activeInfoChanged(respdata.getData().get(key).get(0));
//            }
//        }
    }
    @Override
    public void onLoaderReset(Loader<ActiveInfoRespBean> loader) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewUtils.unbindDrawables(mBoardLayout);


    }
}
