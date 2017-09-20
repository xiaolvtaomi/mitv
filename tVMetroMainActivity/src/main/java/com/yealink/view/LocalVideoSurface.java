package com.yealink.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.yealink.lib.common.wrapper.CallManager;
import com.yealink.lib.debug.DebugLog;
import com.yealink.lib.utils.Utils;


/**
 * 本地视频，未居中显示
 * 
 * @author liurs
 */
public class LocalVideoSurface extends SurfaceView {
    protected static final String TAG = LocalVideoSurface.class.getSimpleName();
    private CallManager.CallListener mCallLsnr = new CallManager.CallAdapter() {
        public void onVideoSizeChanged(int videoType, double ratio) {
            if (videoType == CallManager.TYPE_LOCAL) {
                DebugLog.i(TAG, "onVideoSizeChanged " + ratio);
                if(getParent() != null){
                    getParent().requestLayout();
                }
            }
        };
    };

    public LocalVideoSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        int surfaceId= Utils.genSurfaceId();
        DebugLog.i(TAG,"LocalVideoSurface gen id "+surfaceId);
        setId(surfaceId);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        DebugLog.i(TAG,"onWindowFocusChanged");
        if(hasWindowFocus){
            CallManager.getInstance().resetCamera();
        }
    }
    

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        DebugLog.i(TAG, "onAttachedToWindow");
        CallManager.getInstance().registerCallListener(mCallLsnr, getHandler());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DebugLog.i(TAG, "onDetachedFromWindow");
        CallManager.getInstance().unregisterCallListener(mCallLsnr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        DebugLog.i(TAG, "onMeasure");
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width > 0 && height > 0) {
            float ratio = CallManager.getInstance().getVideoRatio(CallManager.TYPE_LOCAL);
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                ratio = 1f / ratio;
            }
            float layoutRatio = width * 1f / height;
            if (layoutRatio > ratio) {
                width = (int) (height * ratio);
            } else {
                height = (int) (width / ratio);
            }
        }
        if(height % 2 != 0){
            height-=1;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        DebugLog.i(TAG, "onLayout");
        if(isEnabled()){
            super.onLayout(changed, left, top, right, bottom);
            CallManager.getInstance().setLocalVideoView(this);
            CallManager.getInstance().updateCameraOrientation();
        }
        
    }
    
}