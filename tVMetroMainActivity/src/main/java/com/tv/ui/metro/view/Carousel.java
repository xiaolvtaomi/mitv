package com.tv.ui.metro.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tv.ui.metro.R;

import java.util.ArrayList;
import java.util.List;


public class Carousel extends FrameLayout {
    private static final int DELAY = 3000;
    private Context mContext;
    //存放图片的ImageView
    private List<ImageView> mPagesIV;
    //存放圆点的ImageView
    private List<ImageView> mDotsIV;
    //待显示图片的资源ID
    private int[] mDrawableIds = {R.drawable.ic_launcher, R.drawable.icon_h_default, R.drawable.ic_launcher,
            R.drawable.icon_h_default, R.drawable.ic_launcher};
    private ViewPager mVP;
    private boolean isAutoPlay;
    private int currentItem;
    private Handler mHandler = new Handler();

    public Carousel(Context context) {
        this(context, null);
    }

    public Carousel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Carousel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        mPagesIV = new ArrayList<ImageView>();
        mDotsIV = new ArrayList<ImageView>();
        initContent();
    }

    private void initContent() {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.carousel_layout, this, true);
        mVP = (ViewPager) view.findViewById(R.id.view_pager);
        LinearLayout mDotsLayout = (LinearLayout) view.findViewById(R.id.dots);

        int len = 5;
        for (int i = 0; i < len; i++) {
            ImageView dotIV = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = params.rightMargin = 4;
            mDotsLayout.addView(dotIV, params);
            mDotsIV.add(dotIV);
        }

        for (int i = 0; i < len; i++) {
            ImageView pageIV = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            pageIV.setImageResource(mDrawableIds[i]);
            mPagesIV.add(pageIV);
        }

        //禁止手指控制滑动
        mVP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        mVP.setAdapter(new TopPagerAdapter());
        mVP .setFocusable(false);
        mVP.setCurrentItem(currentItem);
        mVP.setOnPageChangeListener(new TopOnPageChangeListener());
        startCarousel();
    }

    private void startCarousel() {
        isAutoPlay = true;
        mHandler.postDelayed(task, DELAY);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (isAutoPlay) {
                currentItem = (currentItem + 1) % (mPagesIV.size());
                mVP.setCurrentItem(currentItem);
                mHandler.postDelayed(task, DELAY);
            } else {
                mHandler.postDelayed(task, DELAY);
            }
        }
    };

    class TopPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagesIV.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mPagesIV.get(position));
            return mPagesIV.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class TopOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < mDotsIV.size(); i++) {
                if (i == position) {
                    mDotsIV.get(i).setImageResource(R.drawable.dot_focus);
                } else {
                    mDotsIV.get(i).setImageResource(R.drawable.dot_blur);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                //SCROLL_STATE_DRAGGING
                case 1:
                    isAutoPlay = false;
                    break;
                //SCROLL_STATE_SETTLING
                case 2:
                    isAutoPlay = true;
                    break;
                default:
                    break;
            }
        }
    }
}

