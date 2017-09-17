package com.tv.ui.metro.view;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.os.SystemClock;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.tv.ui.metro.R;


/**
 * Created by Lenovo on 2017/2/15.
 */
public class MyGiftView extends ImageView{
    private Movie mMovie;//播放动画需要用到的，系统类
    private int mImageWidth;//动画的imageview的宽度
    private int mImageHeight;//动画imageview的高度
    private long mMovieStart = 0;// 播放开始
    private boolean isAutoPlay;//是否自动播放
    /**
     * Scaling factor to fit the animation within view bounds.
     */
    private float mScale;
    /**
     * Scaled movie frames width and height.
     */
    private int mMeasuredGifWidth;
    private int mMeasuredGifHeight;


    public MyGiftView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGiftView(Context context) {
        super(context);
    }
    public MyGiftView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }
    private void init(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs,R.styleable.MyGiftView);
        // 通过反射拿布局中src的资源id,所以gif文件需要放在布局的src中
        // --2017/2/28修改 int resourceId = getResourceId(attributes, context, attrs);
//        int resourceId = getResourceId(attributes, context, attrs);
        int resourceId = getGifResourceId(attributes, context, attrs);
        if (resourceId != 0) {
            // 说明是gif动画
            // 1.将resourcesId变成流
            // 2.用Move来decode解析流
            // 3.获得bitmap的长宽
            InputStream is = getResources().openRawResource(resourceId);
            mMovie = Movie.decodeStream(is);
            if (mMovie != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                mImageWidth = bitmap.getWidth();
                mImageHeight = bitmap.getHeight();
                // 用完释放
                bitmap.recycle();
                // 获得是否允许自动播放，如果不允许自动播放，则初始化播放按钮
                isAutoPlay = true;
            }
        }
        //回收资源
        if (attributes != null) {
            attributes.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMovie != null) {
		/*
		 * Calculate horizontal scaling
		 */
            float scaleW = 1f;
            int measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);
            if (measureModeWidth != MeasureSpec.UNSPECIFIED) {
                int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
                scaleW = (float) mImageWidth / (float) maximumWidth;
            }
		/*
		 * calculate vertical scaling
		 */
            float scaleH = 1f;
            int measureModeHeight = MeasureSpec.getMode(heightMeasureSpec);
            if (measureModeHeight != MeasureSpec.UNSPECIFIED) {
                int maximumHeight = MeasureSpec.getSize(heightMeasureSpec);
                scaleH = (float) mImageHeight / (float) maximumHeight;
            }
		/*
		 * calculate overall scale
		 */
            mScale = 1f / Math.max(scaleH, scaleW);
            mMeasuredGifWidth = (int) (mImageWidth * mScale);
            mMeasuredGifHeight = (int) (mImageHeight * mScale);
            setMeasuredDimension(mMeasuredGifWidth, mMeasuredGifHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mMovie == null) {
            // mMovie等于null，说明是张普通的图片，则直接调用父类的onDraw()方法
            super.onDraw(canvas);
        } else {
            // mMovie不等于null，说明是张GIF图片
            if (isAutoPlay) {
                // 如果允许自动播放，就播放
                requestLayout();
                playMovie(canvas);
                invalidate();
            }

        }
    }

    /**
     * 播放gif动画
     *
     * @param canvas
     */
    private boolean playMovie(Canvas canvas) {
        // 1.获取播放的时间
        // 2.如果开始start=0，则认为是开始
        // 3.记录播放的时间
        // 4.设置进度
        // 5.画动画
        // 6.如果时间大于了播放的时间，则证明结束
        long now = SystemClock.uptimeMillis();
        if (mMovieStart == 0) {
            mMovieStart = now;
        }
        int duration = mMovie.duration();
        if (duration == 0) {
            duration = 1000;
        }
        //记录gif播放了多少时间
        int relTime = (int) ((now - mMovieStart) % duration);
        mMovie.setTime(relTime);// 设置时间
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(mScale, mScale);
        mMovie.draw(canvas, 0, 0);// 画
        canvas.restore();
        if ((now - mMovieStart) >= duration) {
            // 结束
            mMovieStart = 0;
            return true;
        }
        return false;
    }



    /**
     * 通过反射拿布局中src的资源id
     *
     * @param attrs
     * @param context
     * @param attributes
     */
    private int getResourceId(TypedArray attributes, Context context, AttributeSet attrs) {
        try {
            Field filed = TypedArray.class.getDeclaredField("mValue");
            filed.setAccessible(true);
            TypedValue typeValue = (TypedValue) filed.get(attributes);
            return typeValue.resourceId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getGifResourceId(TypedArray attributes, Context context, AttributeSet attrs){
        try {
//            Field filed = TypedArray.class.getDeclaredField("mValue");
//            filed.setAccessible(true);
//            TypedValue typeValue = (TypedValue) filed.get(attributes);
//            return typeValue.resourceId;
            return attributes.getResourceId(R.styleable.MyGiftView_gifSrc, 0 );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setGifResource(final String gifUrl){
        // 说明是gif动画
        // 1.将resourcesId变成流
        // 2.用Move来decode解析流
        // 3.获得bitmap的长宽
        new Thread(){
            public void run(){
                InputStream is = returnGifStream(gifUrl) ;
                mMovie = Movie.decodeStream(is);
//                is.mark(1024);
                if (mMovie != null) {
                    InputStream is2 = returnGifStream(gifUrl) ;
                    Bitmap bitmap = BitmapFactory.decodeStream(is2);
                    mImageWidth = bitmap.getWidth();
                    mImageHeight = bitmap.getHeight();
                    // 用完释放
                    bitmap.recycle();
                    // 获得是否允许自动播放，如果不允许自动播放，则初始化播放按钮
                    isAutoPlay = true;
//                    invalidate();
                            postInvalidate();

                }
            }
        }.start();

    }



    public void setGifResource(int gifResourceId){
        // 说明是gif动画
        // 1.将resourcesId变成流
        // 2.用Move来decode解析流
        // 3.获得bitmap的长宽
        InputStream is = getResources().openRawResource(gifResourceId);
        mMovie = Movie.decodeStream(is);
        if (mMovie != null) {
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            mImageWidth = bitmap.getWidth();
            mImageHeight = bitmap.getHeight();
            // 用完释放
            bitmap.recycle();
            // 获得是否允许自动播放，如果不允许自动播放，则初始化播放按钮
            isAutoPlay = true;
        }
    }


    public static InputStream returnGifStream(String path) {
        URL url = null;
        InputStream is =null;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();//利用HttpURLConnection对象,我们可以从网络中获取网页数据.
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();	//得到网络返回的输入流

        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

}