package com.tv.ui.metro.galleryviewpager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.VolleyHelper;
import com.app.MyApplication;
import com.tv.ui.metro.R;
import com.tv.ui.metro.httpurl.HttpUrl;
import com.tv.ui.metro.model.GoodsInfoBean;

import java.util.ArrayList;
import java.util.List;

public class Gallery2 extends FragmentActivity{
    private ViewPager mViewPager;
    ImageView  qrCode ;
    String id;
    TextView shopNames,commodityNames,commodityDescs,sellingPrices;
    private ImageLoader mImageLoader;
    private List<String> introduce;
    private GalleryAdapter adapter;
    private List<String> mPagesIV ;
    RelativeLayout buypage;
    Animation animIn;
    Animation animOut;
    String commodityImgs;
    String qrcode;
    String commodityName;
    String sellingPrice;
    String shopName;
    String commodityDesc;
    Intent intent;
    String activity_Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery2);
        init();
        intent=getIntent();
        activity_Name=intent.getStringExtra("activity_Name");
        if (!activity_Name.equals("AppMain")){
         qrcode=intent.getStringExtra("qrcode");
         commodityImgs=intent.getStringExtra("commodityImgs");
         commodityName=intent.getStringExtra("commodityName");
         sellingPrice=intent.getStringExtra("sellingPrice");
         shopName=intent.getStringExtra("shopName");
         commodityDesc=intent.getStringExtra("commodityDesc");
         mImageLoader.get(qrcode,ImageLoader.getCommonViewImageListener(qrCode,R.drawable.defeat,R.drawable.defeat));
        mViewPager = (ViewPager) findViewById(R.id.view_pager2);
        mViewPager.setCurrentItem(0);// 设置起始位置
        mPagesIV = new ArrayList<String>();
        introduce = new ArrayList<String>();
        if (!commodityImgs.equals(null)&&!"".equals(commodityImgs)) {
            String[] strarray = commodityImgs.split("[,]");
            for (int i = 0; i < strarray.length; i++)
                mPagesIV.add(strarray[i]);
        }
        else {
            mPagesIV.add("http://res2.esf.leju.com/esf_www/statics/images/default-img/detail.png");
        }
        mViewPager.setAdapter(adapter = new GalleryAdapter(
                getSupportFragmentManager()));
        introduce.add("");
        buypage = (RelativeLayout)findViewById(R.id.buy);
        animIn = AnimationUtils.loadAnimation(Gallery2.this, R.anim.anim_buy_page) ;
        animOut = AnimationUtils.loadAnimation(Gallery2.this, R.anim.anim_buy_page_kill) ;
       if (!commodityName.equals(null)&&!"".equals(commodityName)){
           commodityNames.setText(commodityName);
       }
        if (!sellingPrice.equals(null)&&!"".equals(sellingPrice)){
            sellingPrices.setText(sellingPrice);
        }
        if (!shopName.equals(null)&&!"".equals(shopName)){
            shopNames.setText(shopName);
        }
        if (!commodityDesc.equals(null)&&!"".equals(commodityDesc)){
            commodityDescs.setText(commodityDesc);
        }
    }else {
            in();
        }
    }


    public void  init(){
        mImageLoader = VolleyHelper.getInstance(getApplicationContext().getApplicationContext()).getImageLoader();
        qrCode= (ImageView) findViewById(R.id.qrcode);
        shopNames= (TextView) findViewById(R.id.shopName);
        commodityNames= (TextView) findViewById(R.id.commodityName);
        commodityDescs= (TextView) findViewById(R.id.commodityDesc);
        sellingPrices= (TextView) findViewById(R.id.sellingPrice);

    }
    public  void in(){
       id=intent.getStringExtra("id");
        String path= HttpUrl.goods_details+id;
        final StringRequest request = new StringRequest(Request.Method.GET, path, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                qrcode= GoodsInfoBean.parseData(s).getQrCode();
                commodityImgs= GoodsInfoBean.parseData(s).getCommodityImgs();
                commodityName= GoodsInfoBean.parseData(s).getCommodityName();
                sellingPrice= GoodsInfoBean.parseData(s).getSellingPrice();
                shopName= GoodsInfoBean.parseData(s).getShopName();
                commodityDesc= GoodsInfoBean.parseData(s).getCommodityDesc();
                if(!qrcode.equals("null")&&!"".equals(qrcode)&&qrcode!=null) {
                    mImageLoader.get(qrcode, ImageLoader.getCommonViewImageListener(qrCode, R.drawable.defeat, R.drawable.defeat));
                }else{
                    qrCode.setImageResource(R.drawable.defeat);
                }
                mViewPager = (ViewPager) findViewById(R.id.view_pager2);
                mViewPager.setCurrentItem(0);// 设置起始位置
                mPagesIV = new ArrayList<String>();
                introduce = new ArrayList<String>();
                if (commodityImgs!=null&&!"".equals(commodityImgs)) {
                    String[] strarray = commodityImgs.split("[,]");
                    for (int i = 0; i < strarray.length; i++)
                        mPagesIV.add(strarray[i]);
                }
                else {
                    mPagesIV.add("http://res2.esf.leju.com/esf_www/statics/images/default-img/detail.png");
                }
                mViewPager.setAdapter(adapter = new GalleryAdapter(
                        getSupportFragmentManager()));
                introduce.add("");
                buypage = (RelativeLayout)findViewById(R.id.buy);
                animIn = AnimationUtils.loadAnimation(Gallery2.this, R.anim.anim_buy_page) ;
                animOut = AnimationUtils.loadAnimation(Gallery2.this, R.anim.anim_buy_page_kill) ;
                if (!commodityName.equals(null)&&!"".equals(commodityName)){
                    commodityNames.setText(commodityName);
                }
                if (sellingPrice!=null&&!"".equals(sellingPrice)){
                    sellingPrices.setText(sellingPrice);
                }
                if (shopName!=null&&!"".equals(shopName)){
                    shopNames.setText(shopName);
                }
                if (commodityDesc!=null&&!"".equals(commodityDesc)){
                    commodityDescs.setText(commodityDesc);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.getHttpQueue().add(request);
    }

    class GalleryAdapter extends FragmentPagerAdapter {
        public GalleryAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return mPagesIV.size();
        }
        @Override
        public Fragment getItem(int position) {
            return ItemFragment.create(String.format("", position),mPagesIV.get(position),position,mPagesIV.size());
        }

    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (
                mViewPager.getCurrentItem()==mViewPager.getAdapter().getCount()-1
                        && buypage.getVisibility() != View.VISIBLE
                        && event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT
                        &&!commodityImgs.equals(null)
                        &&!"".equals(commodityImgs) ){
            buypage.setAnimation(animIn);
            buypage.startAnimation(animIn);
            buypage.setVisibility(View.VISIBLE);
            return  true;
        }
        if (
                mViewPager.getCurrentItem()==mViewPager.getAdapter().getCount()-1
                        && buypage.getVisibility() == View.VISIBLE
                        && event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT
                        &&!commodityImgs.equals(null)
                        &&!"".equals(commodityImgs)){
            buypage.setAnimation(animOut);
            buypage.startAnimation(animOut);
            buypage.setVisibility(View.GONE);
            return  true;
        }
       else if(mViewPager.getCurrentItem()!=mViewPager.getAdapter().getCount()-1&&buypage.getVisibility() == View.VISIBLE){
            buypage.setVisibility(View.GONE);
            buypage.clearAnimation();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}