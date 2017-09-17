package com.tv.ui.metro.view;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.tv.ui.metro.R;
import com.tv.ui.metro.Utils;
import com.tv.ui.metro.model.DisplayItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BoardLayout extends FrameLayout implements View.OnFocusChangeListener{
    Context mContext;
    static  int DIVIDE_SIZE = 20;
    boolean mMirror = false;
    AnimatorSet mScaleAnimator;
    List<WeakReference<View>> mViewList = new ArrayList<WeakReference<View>>();
    HashMap<View, WeakReference<MirrorItemView>> mViewMirrorMap = new HashMap<View, WeakReference<MirrorItemView>>();
    MetroCursorView mMetroCursorView;
    WindowManager wm = (WindowManager) getContext()
            .getSystemService(Context.WINDOW_SERVICE);
    int width = wm.getDefaultDisplay().getWidth();
    int height = wm.getDefaultDisplay().getHeight();
    View mLeftView;
    View mRightView;
    View mUpView;
    int w;
    int h;

    float mDensityScale = 1.0f;
    private static int ITEM_BOARD_WIDTH  = -1;
    private static int ITEM_BOARD_HEIGHT = -1;
    public class Item{
        public Item( int type, int row){
            mType = type;
            mRow = row;
        }
        public int mType;
        public int mRow;
    }

    public BoardLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public BoardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public  void init(){

        if (ITEM_BOARD_WIDTH == -1) {
            DIVIDE_SIZE = getResources().getDimensionPixelSize(R.dimen.ITEM_BOARD_DIVIDE_SIZE);

            if (w <= 0 || h <= 0) {
                // 说明还没有负值 宽高的格子数
                ITEM_BOARD_WIDTH = getResources().getDimensionPixelSize(R.dimen.ITEM_BOARD_WIDTH);
                ITEM_BOARD_HEIGHT = getResources().getDimensionPixelSize(R.dimen.ITEM_BOARD_HEIGHT);
            }
        }else {
            if(w > 0 && h > 0){
                ITEM_BOARD_WIDTH = (width - getResources().getDimensionPixelSize(R.dimen.ITEM_D_WIDTH) - (w - 1) * DIVIDE_SIZE) / w;
                ITEM_BOARD_HEIGHT = (height - getResources().getDimensionPixelSize(R.dimen.ITEM_TOP_HEIGHT) - (h - 1) * DIVIDE_SIZE) / h;
            }
        }

        mDensityScale = 1;//mContext.getResources().getDisplayMetrics().densityDpi/320.0f;
        setClipChildren(false);
        setClipToPadding(false);
    }

    public View getItemView(int index){
        if(index>=mViewList.size()) return null;
        return mViewList.get(index).get();
    }

    public View addItemView(View child, int x, int y , int wc , int hc,String focusable){
        return addItemView(child, x,y,wc,hc, DIVIDE_SIZE,focusable);
    }

    public void clearItems(){
        removeAllViews();
        mViewList.clear();
        mViewMirrorMap.clear();
        mLeftView = null;
        mUpView=null;
        mRightView = null;
    }
   public void  setShape(int wc, int hc){
            w = wc;
            h = hc;
            init();
         }
    public View addItemView(View child, int x, int y , int wc , int hc, int padding,String focusable){
        if(mLeftView==null){
            mLeftView = child;
        }
        if(mUpView==null && y+hc == h+1&&focusable.equals("1") ){
            mUpView = child;
        }

        if(mRightView==null) {
            mRightView = child;
        }
        child.setFocusable(true);
        child.setOnFocusChangeListener(this);
        LayoutParams flp;
        mViewList.add(new WeakReference<View>(child));
        View result = child;

        flp = new LayoutParams(
                (int)(ITEM_BOARD_WIDTH*mDensityScale*wc+(wc-1)*DIVIDE_SIZE),
                (int)(ITEM_BOARD_HEIGHT*mDensityScale*hc+(hc-1)*DIVIDE_SIZE));

        Log.e("BoardLayout", "x="+x+";y="+y);

        if (focusable.equals("1")){
        child.setFocusable(true);}
        else {
            child.setFocusable(false);
        }
        child.setOnFocusChangeListener(this);
        child.setTag(R.integer.tag_view_postion, y+hc);
//			flp.leftMargin = x>1?DIVIDE_SIZE:0;
//			flp.topMargin = y>1?DIVIDE_SIZE:0;

        flp.leftMargin = (x-1)*(ITEM_BOARD_WIDTH + DIVIDE_SIZE);
        flp.topMargin = (y-1)*(ITEM_BOARD_HEIGHT + DIVIDE_SIZE);
        addView(child, flp);

        return result;
    }

    private View lastFocusedView;

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (lastFocusedView!=null&&lastFocusedView.requestFocus(direction, previouslyFocusedRect)) {
            return true;
        }

        int index;
        int increment;
        int end;
        int count = this.getChildCount();
        if ((direction & FOCUS_FORWARD) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }

        for (int i = index; i != end; i += increment) {
            View child = this.getChildAt(i);
            {
                if (child.requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
        }
        return false;
    }
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child,focused);
    }
    public void onFocusChange(final View v, boolean hasFocus){
        if(mScaleAnimator!=null) mScaleAnimator.end();
        if(mMetroCursorView!=null){
            if(hasFocus){
                if(mViewMirrorMap.get(v)!=null){
                    mMetroCursorView.setFocusView(mViewMirrorMap.get(v).get());
                }else{
                    mMetroCursorView.setFocusView(v);
                }
                v.setTag(R.integer.tag_view_focused_host_view, mMetroCursorView);
                lastFocusedView = v;
            }else{
                if(mViewMirrorMap.get(v)!=null){
                    mMetroCursorView.setUnFocusView(mViewMirrorMap.get(v).get());
                }else{
                    mMetroCursorView.setUnFocusView(v);
                }
            }
        }else{
            if(hasFocus){
                lastFocusedView = v;
                bringChildToFront(v);
                invalidate();
                ObjectAnimator animX = ObjectAnimator.ofFloat(v, "ScaleX",
                        new float[] { 1.0F, 1.1F }).setDuration(200);
                ObjectAnimator animY = ObjectAnimator.ofFloat(v, "ScaleY",
                        new float[] { 1.0F, 1.1F }).setDuration(200);
                mScaleAnimator = new AnimatorSet();
                mScaleAnimator.playTogether(new Animator[] { animX, animY });
                mScaleAnimator.start();

                //v.setScaleX(1.1f);
                //v.setScaleY(1.1f);
            }else{
                v.setScaleX(1.0f);
                v.setScaleY(1.0f);
            }
        }

    }

    public void setMetroCursorView(MetroCursorView v){
        mMetroCursorView = v;
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Handle automatic focus changes.
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int direction = 0;
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_LEFT;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_RIGHT;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_UP;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_DOWN;
                    }
                    break;
                case KeyEvent.KEYCODE_TAB:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_FORWARD;
                    } else if (event.hasModifiers(KeyEvent.META_SHIFT_ON)) {
                        direction = View.FOCUS_BACKWARD;
                    }
                    break;
            }
            if (direction == View.FOCUS_DOWN || direction == View.FOCUS_UP) {
                View focused = findFocus();
                if (focused != null) {
                    View v = focused.focusSearch(direction);
                    if (v == null) {
                        Utils.playKeySound(this, Utils.SOUND_ERROR_KEY);
                        // mMetroCursorView.showIndicator();
                        return true;
                    }
                }
            }
        }

        boolean ret = super.dispatchKeyEvent(event);
        return ret;
    }

    public void focusMoveToLeft(){
        mLeftView.requestFocus();
    }

    public void focusMoveToUp() {
        if (mUpView!=null)
        mUpView.requestFocus();
    }
    public void focusMoveToRight(){
        mRightView.requestFocus();
    }

    public void focusMoveToPreFocused(){
        if(lastFocusedView!=null){
            lastFocusedView.requestFocus();
        }else {
            mLeftView.requestFocus();
        }
    }
}
