package com.tv.ui.metro.galleryviewpager;

import android.app.Activity;
import android.os.Bundle;

import com.tv.ui.metro.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Gallery extends FragmentActivity {
    private GalleryViewPager mViewPager;
    private GalleryAdapter adapter;
    private List<String> mPagesIV ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mViewPager = (GalleryViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(adapter = new GalleryAdapter(
                getSupportFragmentManager()));
        mViewPager.setPageMargin(30);// 设置页面间距
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(0);// 设置起始位置
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mPagesIV= new ArrayList<String>();
        mPagesIV.add("http://img.ivsky.com/img/tupian/pre/201612/02/ningjing_de_hupo-002.jpg");
        mPagesIV.add("http://img.ivsky.com/img/tupian/pre/201612/02/ningjing_de_hupo.jpg");
        mPagesIV.add("http://img.ivsky.com/img/tupian/pre/201612/02/ningjing_de_hupo-001.jpg");
        mPagesIV.add("http://img.ivsky.com/img/tupian/pre/201612/02/ningjing_de_hupo-003.jpg");
        mPagesIV.add("http://img.ivsky.com/img/tupian/pre/201612/02/ningjing_de_hupo-004.jpg");
        mPagesIV.add("http://img.ivsky.com/img/tupian/pre/201612/02/ningjing_de_hupo-005.jpg");
    }

    class GalleryAdapter extends FragmentPagerAdapter {
        public GalleryAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return 6;
        }
        @Override
        public Fragment getItem(int position) {
            return ItemFragment.create(String.format("这里是第%d页", position),mPagesIV.get(position),position,mPagesIV.size());
        }



    }

}
