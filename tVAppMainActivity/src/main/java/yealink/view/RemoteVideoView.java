package yealink.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceView;
import android.widget.FrameLayout;

/**
 * 远程视频，自动居中
 * 
 * @author liurs
 */
public class RemoteVideoView extends FrameLayout {
    private RemoteVideoSurface mView;

    public RemoteVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        setBackgroundColor(0xff000000);
        
        mView = new RemoteVideoSurface(context, attrs);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        addView(mView, params);
    }

    public SurfaceView getSurface(){
        return mView;
    }
    
    /**
     * @return 远方视频、或者屏幕分享类型
     */
    public int getVideoType() {
        return mView.getVideoType();
    }

    /**
     * 设置是否本地视频，还是远方视频
     * 
     * @param
     */
    public void setVideoType(int videoType) {
        mView.setVideoType(videoType);
    }
}
