package com.yealink.view;//package com.yealink.view;
//
//import android.content.Context;
//import android.content.res.Configuration;
//import android.graphics.Rect;
//import android.media.AudioManager;
//import android.media.ToneGenerator;
//import android.os.AsyncTask;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.MeasureSpec;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.EditText;
//import android.widget.PopupWindow;
//import android.widget.PopupWindow.OnDismissListener;
//
//import com.yealink.lib.common.wrapper.CallManager;
//import com.yealink.lib.debug.DebugLog;
//
//import unicom.cpsuiyang.R;
//
//
///**
// * 用于通话界面中的DTMF输入用的PopupWindow
// *
// * @author liurs
// *
// */
//public class DtmfWindow implements OnClickListener {
//    private static final String TAG = DtmfWindow.class.getSimpleName();
//
//    private static boolean USE_SYSTEM_DTMF = false;
//
//    private Context mContext;
//
//    private View mLayout;
//
//    private View mNumber0;
//    private View mNumber1;
//    private View mNumber2;
//    private View mNumber3;
//    private View mNumber4;
//    private View mNumber5;
//    private View mNumber6;
//    private View mNumber7;
//    private View mNumber8;
//    private View mNumber9;
//    private View mDot;
//    /** #号 **/
//    private View mSharp;
//
//    private PopupWindow mWindow;
//
//    private EditText mEditText;
//
//    /** 弹框底部小箭头 **/
//    private View mIndicatorContainer;
//    private View mIndicator;
//
//    private ToneGenerator mTone;
//
//    private int mWindowMarginBottom;
//    private int mMaxWidth;
//    private int mMaxHeight;
//
//    private StringBuilder mDefaultString;
//
//    private int mScreenOrientation;
//
//    public DtmfWindow(Context context, StringBuilder defaultString) {
//        mContext = context;
//        mDefaultString = defaultString;
//
//        mScreenOrientation = context.getResources().getConfiguration().orientation;
//
//        mWindowMarginBottom = context.getResources().getDimensionPixelSize(R.dimen.talking_pop_window_margin_bottom);
//
//        mMaxWidth = context.getResources().getDimensionPixelSize(R.dimen.dtmf_width);
//        mMaxHeight = context.getResources().getDimensionPixelSize(R.dimen.dtmf_height);
//
//        mTone = new ToneGenerator(AudioManager.STREAM_DTMF, 80);
//
//        mLayout = LayoutInflater.from(mContext).inflate(R.layout.dtmf_window, null);
//
//        mEditText = (EditText) mLayout.findViewById(R.id.editText);
//
//        // 各个按键
//        mNumber0 = mLayout.findViewById(R.id.number0);
//        mNumber0.setOnClickListener(this);
//        mNumber1 = mLayout.findViewById(R.id.number1);
//        mNumber1.setOnClickListener(this);
//        mNumber2 = mLayout.findViewById(R.id.number2);
//        mNumber2.setOnClickListener(this);
//        mNumber3 = mLayout.findViewById(R.id.number3);
//        mNumber3.setOnClickListener(this);
//        mNumber4 = mLayout.findViewById(R.id.number4);
//        mNumber4.setOnClickListener(this);
//        mNumber5 = mLayout.findViewById(R.id.number5);
//        mNumber5.setOnClickListener(this);
//        mNumber6 = mLayout.findViewById(R.id.number6);
//        mNumber6.setOnClickListener(this);
//        mNumber7 = mLayout.findViewById(R.id.number7);
//        mNumber7.setOnClickListener(this);
//        mNumber8 = mLayout.findViewById(R.id.number8);
//        mNumber8.setOnClickListener(this);
//        mNumber9 = mLayout.findViewById(R.id.number9);
//        mNumber9.setOnClickListener(this);
//        mDot = mLayout.findViewById(R.id.dot);
//        mDot.setOnClickListener(this);
//        mSharp = mLayout.findViewById(R.id.sharp);
//        mSharp.setOnClickListener(this);
//
//        mIndicatorContainer = mLayout.findViewById(R.id.indicatorContainer);
//        mIndicator = mLayout.findViewById(R.id.indicator);
//
//        mWindow = new PopupWindow(context);
//        mWindow.setBackgroundDrawable(null);
//        mWindow.setContentView(mLayout);
//        mWindow.setAnimationStyle(R.style.anim_pop_window);
//    }
//
//    /**
//     * @return 弹框是否显示
//     */
//    public boolean isShowing() {
//        return mWindow.isShowing();
//    }
//
//    /**
//     * 显示于指定View之上
//     *
//     * @param anchor
//     * @param marginTop
//     */
//    public void showUpOf(View anchor, int marginTop) {
//        if(mScreenOrientation == Configuration.ORIENTATION_PORTRAIT){
//            mWindow.setWindowLayoutMode(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//
//            mLayout.measure(MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE, MeasureSpec.UNSPECIFIED),
//                    MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST));
//
//            mWindow.setHeight(mLayout.getMeasuredHeight());
//
//            mEditText.append(mDefaultString.toString());
//
//            DebugLog.i(TAG, "width:" + mLayout.getMeasuredWidth() + " height:" + mLayout.getMeasuredHeight());
//
//            // 获取View的位置
//            int[] anchorLocation = new int[2];
//            anchor.getLocationInWindow(anchorLocation);
//
//            // 获取窗口大小
//            final Rect displayFrame = new Rect();
//            anchor.getWindowVisibleDisplayFrame(displayFrame);
//
//            // 计算弹窗的左上角位置
//            int left = 0;
//            int top = anchorLocation[1] - mLayout.getMeasuredHeight() - mWindowMarginBottom;
//
//            // 移动小箭头到父View的中间
//            int indicatorLeft = anchorLocation[0] + anchor.getWidth() / 2
//                    - left - mIndicator.getMeasuredWidth() / 2;
//            mIndicatorContainer.setPadding(indicatorLeft, 0, 0, 0);
//
//            mWindow.showAtLocation(anchor, Gravity.TOP | Gravity.LEFT, left, top);
//        } else {
//            mLayout.measure(MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.UNSPECIFIED),
//                    MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST));
//
//            mWindow.setWidth(mMaxWidth);
//            mWindow.setHeight(mLayout.getMeasuredHeight());
//
//            mEditText.append(mDefaultString.toString());
//
//            DebugLog.i(TAG, "width:" + mLayout.getMeasuredWidth() + " height:" + mLayout.getMeasuredHeight());
//
//            // 获取View的位置
//            int[] anchorLocation = new int[2];
//            anchor.getLocationInWindow(anchorLocation);
//
//            // 获取窗口大小
//            final Rect displayFrame = new Rect();
//            anchor.getWindowVisibleDisplayFrame(displayFrame);
//
//            // 计算弹窗的左上角位置
//            int left = anchorLocation[0] - (mMaxWidth - anchor.getWidth()) / 2;
//            int top = anchorLocation[1] - mLayout.getMeasuredHeight() - mWindowMarginBottom;
//
//            // 移动小箭头到父View的中间
//            int indicatorLeft = (mMaxWidth - mIndicator.getMeasuredWidth()) / 2;
//            mIndicatorContainer.setPadding(indicatorLeft, 0, 0, 0);
//
//            mWindow.showAtLocation(anchor, Gravity.TOP | Gravity.LEFT, left, top);
//        }
//    }
//
//    /**
//     * 关闭Window
//     */
//    public void dismiss(){
//        mWindow.dismiss();
//    }
//
//    /**
//     * 监听Dismiss事件
//     *
//     * @param lsnr
//     */
//    public void setOnDismissListener(OnDismissListener lsnr) {
//        mWindow.setOnDismissListener(lsnr);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.number0:
//                startTone(ToneGenerator.TONE_DTMF_0);
//                onInput("0");
//                break;
//            case R.id.number1:
//                startTone(ToneGenerator.TONE_DTMF_1);
//                onInput("1");
//                break;
//            case R.id.number2:
//                startTone(ToneGenerator.TONE_DTMF_2);
//                onInput("2");
//                break;
//            case R.id.number3:
//                startTone(ToneGenerator.TONE_DTMF_3);
//                onInput("3");
//                break;
//            case R.id.number4:
//                startTone(ToneGenerator.TONE_DTMF_4);
//                onInput("4");
//                break;
//            case R.id.number5:
//                startTone(ToneGenerator.TONE_DTMF_5);
//                onInput("5");
//                break;
//            case R.id.number6:
//                startTone(ToneGenerator.TONE_DTMF_6);
//                onInput("6");
//                break;
//            case R.id.number7:
//                startTone(ToneGenerator.TONE_DTMF_7);
//                onInput("7");
//                break;
//            case R.id.number8:
//                startTone(ToneGenerator.TONE_DTMF_8);
//                onInput("8");
//                break;
//            case R.id.number9:
//                startTone(ToneGenerator.TONE_DTMF_9);
//                onInput("9");
//                break;
//            case R.id.dot:
//                startTone(ToneGenerator.TONE_DTMF_P);
//                onInput("*");
//                break;
//            case R.id.sharp:
//                startTone(ToneGenerator.TONE_DTMF_S);
//                onInput("#");
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void startTone(int key) {
//        if(USE_SYSTEM_DTMF){
//            new ToneTask(key).execute();
//        }
//    }
//
//    private class ToneTask extends AsyncTask<Void, Void, Void> {
//        private int mKey;
//
//        public ToneTask(int key) {
//            mKey = key;
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            mTone.startTone(mKey, 100);
//            return null;
//        }
//    }
//
//    private void onInput(String number) {
//        DebugLog.i(TAG, "onInput " + number);
//        mEditText.append(number);
//        mDefaultString.append(number);
//        CallManager.getInstance().sendDTMF(number, !USE_SYSTEM_DTMF);
//    }
//}
