package yealink.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceView;
import android.widget.FrameLayout;

/**
 * 本地视频，自动居中
 * 
 * @author liurs
 */
public class LocalVideoView extends FrameLayout {
    private LocalVideoSurface mView;

    public LocalVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        setBackgroundColor(0xff000000);
        
        mView = new LocalVideoSurface(context, attrs);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        addView(mView, params);
    }

    public SurfaceView getSurface(){
        return mView;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mView.setEnabled(enabled);
    }
}
