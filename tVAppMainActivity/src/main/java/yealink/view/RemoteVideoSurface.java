package yealink.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.yealink.lib.common.wrapper.CallManager;
import com.yealink.lib.debug.DebugLog;
import com.yealink.lib.utils.Utils;

import org.webrtc.videoengine.ViEAndroidGLES20;

/**
 * 远程视频，未居中显示
 * 
 * @author liurs
 */
public class RemoteVideoSurface extends ViEAndroidGLES20 {
    private static final String TAG = RemoteVideoSurface.class.getSimpleName();
    
    private int mVideoType;

    private CallManager.CallListener mCallLsnr = new CallManager.CallAdapter() {
        public void onVideoSizeChanged(int videoType, double ratio) {
            if (videoType == mVideoType) {
                DebugLog.i(TAG, "onVideoSizeChanged " + ratio + " type:" + mVideoType);
                post(new Runnable() {
                    @Override
                    public void run() {
                        setAspectRatio(CallManager.getInstance().getVideoRatio(mVideoType));
                    }
                });
            }
        };
    };

    public RemoteVideoSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        setId(Utils.genSurfaceId());
    }
    
    public void setupVideoByType() {
        switch (mVideoType) {
            case CallManager.TYPE_REMOTE:
                CallManager.getInstance().setRemoteVideoView(this);
                break;
            case CallManager.TYPE_SHARE:
                CallManager.getInstance().setScreenShareView(this);
                break;
            case CallManager.TYPE_NONE:
                break;
            default:
                DebugLog.e(TAG, "unsupport video type " + mVideoType);
                break;
        }
    }

    /**
     * @return 远方视频、或者屏幕分享类型
     */
    public int getVideoType() {
        return mVideoType;
    }
    
    /**
     * 设置是否本地视频，还是远方视频
     * 
     * @param
     */
    public void setVideoType(int videoType) {
        mVideoType = videoType;
        if (isShown()) {
            setupVideoByType();
        }
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        DebugLog.i(TAG, "onAttachedToWindow");
        CallManager.getInstance().registerCallListener(mCallLsnr, getHandler());
        setAspectRatio(CallManager.getInstance().getVideoRatio(mVideoType));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DebugLog.i(TAG, "onDetachedFromWindow");
        CallManager.getInstance().unregisterCallListener(mCallLsnr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = right - left;
        int height = bottom - top;

        DebugLog.i(TAG, "onLayout w:" + width + " h:" + height + " type:" + mVideoType);
        if (width > 0 && height > 0) {
            setupVideoByType();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        DebugLog.i(TAG, "surfaceCreated w:" + getWidth() + " h:" + getHeight() + " type:" + mVideoType);
        setupVideoByType();
        super.surfaceCreated(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        DebugLog.i(TAG, "surfaceChanged w:" + width + " h:" + height + " type:" + mVideoType);
        super.surfaceChanged(holder, format, width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        DebugLog.i(TAG, "surfaceDestroyed");
        super.surfaceDestroyed(holder);
    }
}