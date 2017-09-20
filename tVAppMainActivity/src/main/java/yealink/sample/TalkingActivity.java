package yealink.sample;

import android.app.Activity;
import android.bluetooth.BluetoothHeadset;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tv.ui.metro.sampleapp.R;
import com.yealink.lib.common.wrapper.CallManager;
import com.yealink.lib.common.wrapper.CallParams;
import com.yealink.lib.common.wrapper.CallSession;
import com.yealink.lib.debug.DebugLog;

import yealink.view.LocalVideoView;
import yealink.view.RemoteVideoView;


/**
 * Created by chenhn on 2017/1/10.
 */

public class TalkingActivity extends Activity implements View.OnClickListener, BluetoothNotifier.BluetoothListener, HeadsetNotifier.HeadsetListener {

    public static final String EXTRA_CALL_PARAMS = "CallParams";
    public static final String EXTRA_IS_INCOMING = "IsIncoming";
    private static final String TAG = "TalkingActivity";

    private FrameLayout mSmallVideoContainer;
    private FrameLayout mBigVideoContainer;
    private LocalVideoView mLocalVideo;
    private RemoteVideoView mRemoteVideo;
    private TextView mStatus;
    private Button mHangupBtn;
    private Button mPickUpBtn;
    private AudioManager mAudioManager;

    private CallErrorCodeUtil mErrorCodeUtil;

    private static boolean mShowing;
    private boolean mHeadsetPlugged;
    private boolean mBluetoothHeadsetPlugged;
    private boolean mIsIncoming;
    private int mCallId;
    private boolean mIsEstablished = false;

    private CallManager.CallAdapter mCallAdapter = new CallManager.CallAdapter() {

        @Override
        public void onIncomingCall(CallSession session) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (isFinishing()) {
                    DebugLog.e(TAG, "onIncomingCall when activity is shuting down");
                    return;
                }
            }
            mCallId = session.getId();
        };

        @Override
        public void onCallEstablished(final CallSession session) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "onCallEstablished");
                    Toast.makeText(TalkingActivity.this, "已建立通话！", Toast.LENGTH_LONG).show();
                    mCallId = session.getId();
                    mStatus.setText("");
                    mPickUpBtn.setVisibility(View.GONE);
                    mHangupBtn.setVisibility(View.VISIBLE);
                    setVolumn(80);
                    headsetChanged();
                    showVideo();
                    mIsEstablished = true;
                }
            });
        }

        @Override
        public void onCallFailed(final CallSession session, final int errorCode, final int extraCode) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mErrorCodeUtil.setErrorCode(errorCode);
                    mErrorCodeUtil.setExtraCode(extraCode);
                    if (!session.hangupByMe) {
                        if (session.isIncoming) {
                            mErrorCodeUtil.setHangupType(CallErrorCodeUtil.TYPE_BY_HIM);
                        } else {
                            mErrorCodeUtil.setHangupType(CallErrorCodeUtil.TYPE_FAILED);
                        }
                    } else {
                        int duration = (int) ((System.currentTimeMillis() - session.establishTimeMills) / 1000);
                        duration = session.establishTimeMills == 0 ? 0 : duration;
                        mErrorCodeUtil.setHangupType(CallErrorCodeUtil.TYPE_BY_ME);
                        mErrorCodeUtil.setDuration(duration);
                    }
                    Toast.makeText(TalkingActivity.this, mErrorCodeUtil.getString(), Toast.LENGTH_LONG).show();
                    TalkingActivity.this.finish();
                }
            });
        }

        @Override
        public void onCallFinished(final CallSession session, final int errorCode, final int extraCode) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mErrorCodeUtil.setErrorCode(errorCode);
                    mErrorCodeUtil.setExtraCode(extraCode);
                    int duration = (int) ((System.currentTimeMillis() - session.establishTimeMills) / 1000);
                    duration = session.establishTimeMills == 0 ? 0 : duration;
                    mErrorCodeUtil.setDuration(duration);
                    if (!session.hangupByMe) {
                        mErrorCodeUtil.setHangupType(CallErrorCodeUtil.TYPE_BY_HIM);
                    } else {
                        mErrorCodeUtil.setHangupType(CallErrorCodeUtil.TYPE_BY_ME);
                    }
                    Toast.makeText(TalkingActivity.this, mErrorCodeUtil.getString(), Toast.LENGTH_LONG).show();
                    TalkingActivity.this.finish();
                }
            });
        }

        @Override
        public void onConnecting(final CallSession session) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mStatus.setText("连接中...");
                    mCallId = session.getId();
                }
            });

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talking);
        mSmallVideoContainer = (FrameLayout) findViewById(R.id.local_video_container);
        mBigVideoContainer = (FrameLayout) findViewById(R.id.remote_video_container);

        mHangupBtn = (Button) findViewById(R.id.hangup);
        mPickUpBtn = (Button) findViewById(R.id.pickup);
        mStatus = (TextView) findViewById(R.id.status);

        mErrorCodeUtil = new CallErrorCodeUtil(this);

        CallManager.getInstance().setApplicationForeground(true);

        CallManager.getInstance().registerCallListener(mCallAdapter);
        mHangupBtn.setOnClickListener(this);
        mPickUpBtn.setOnClickListener(this);

        mIsIncoming = getIntent().getBooleanExtra(EXTRA_IS_INCOMING, false);
        CallParams ps = getIntent().getParcelableExtra(EXTRA_CALL_PARAMS);

        // 判断是否是来电
        if (mIsIncoming) {
            mCallId = CallManager.getInstance().getCurrentSession().getId();
            mPickUpBtn.setVisibility(View.VISIBLE);
            mHangupBtn.setVisibility(View.GONE);
        } else {
            mCallId = CallManager.getInstance().call(ps);
            mStatus.setText("连接中...");
        }

        BluetoothNotifier.getInstance().registerListener(this);
        HeadsetNotifier.getInstance().registerListener(this);

        setVolumn(80);

        // 是否有带耳机
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mHeadsetPlugged = mAudioManager.isWiredHeadsetOn();
        mBluetoothHeadsetPlugged = mAudioManager.isBluetoothA2dpOn();
        headsetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_talking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mIsEstablished) {
            switch (item.getItemId()) {
                case R.id.action_switch_position:
                    onClickSmallVideo();
                    break;
                case R.id.action_switch_camera:
                    CallManager.getInstance().switchCamera();
                    break;
                case R.id.action_mute:
                    onClickMute();
                    break;
                case R.id.action_disable_camera:
                    onClickStopVideo();
                    break;
                case R.id.action_fullScreen:
                    if (mSmallVideoContainer.getVisibility() == View.VISIBLE) {
                        mSmallVideoContainer.setVisibility(View.INVISIBLE);
                        mHangupBtn.setVisibility(View.INVISIBLE);
                    } else {
                        mSmallVideoContainer.setVisibility(View.VISIBLE);
                        mHangupBtn.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean isShowing() {
        return mShowing;
    }

    private void showVideo() {
        if (mLocalVideo == null) {
            DebugLog.i(TAG, "showLocalVideo");
            mLocalVideo = new LocalVideoView(this, null);
            mLocalVideo.getSurface().setZOrderMediaOverlay(true);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            mSmallVideoContainer.addView(mLocalVideo, params);
            mLocalVideo.requestLayout();
        }
        if (mRemoteVideo == null) {
            DebugLog.i(TAG, "showLocalVideo");
            mRemoteVideo = new RemoteVideoView(this, null);
            mRemoteVideo.setVideoType(CallManager.TYPE_REMOTE);
            mRemoteVideo.getSurface().setZOrderMediaOverlay(false);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            mBigVideoContainer.addView(mRemoteVideo, params);
        }
        if (!CallManager.getInstance().isCameraAvailable()) {
            // 摄像头被占用时，复位一下
            CallManager.getInstance().resetCamera();
        }
        // CallManager.getInstance().switchCamera();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hangup:
                CallManager.getInstance().hangUp(mCallId);
                break;
            case R.id.pickup:
                CallManager.getInstance().answer(mCallId);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShowing = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShowing = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallManager.getInstance().unregisterCallListener(mCallAdapter);
        HeadsetNotifier.getInstance().unregisterListener(this);
        BluetoothNotifier.getInstance().unregisterListener(this);
    }

    private boolean mSmallVideoLocal = true;

    private void onClickSmallVideo() {
        DebugLog.i(TAG, "onClickSmallVideo " + mSmallVideoLocal);

        mSmallVideoLocal = !mSmallVideoLocal;
        if (mSmallVideoLocal) {
            mSmallVideoContainer.removeView(mRemoteVideo);
            mBigVideoContainer.removeView(mLocalVideo);

            mRemoteVideo = new RemoteVideoView(this, null);
            mRemoteVideo.setVideoType(CallManager.TYPE_REMOTE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

            mLocalVideo.getSurface().setZOrderMediaOverlay(true);
            mRemoteVideo.getSurface().setZOrderMediaOverlay(false);

            mSmallVideoContainer.addView(mLocalVideo, 0, params);
            mBigVideoContainer.addView(mRemoteVideo);
        } else {
            mSmallVideoContainer.removeView(mLocalVideo);
            mBigVideoContainer.removeView(mRemoteVideo);

            mRemoteVideo = new RemoteVideoView(this, null);
            mRemoteVideo.setVideoType(CallManager.TYPE_REMOTE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

            mLocalVideo.getSurface().setZOrderMediaOverlay(false);
            mRemoteVideo.getSurface().setZOrderMediaOverlay(true);

            mSmallVideoContainer.addView(mRemoteVideo, 0, params);
            mBigVideoContainer.addView(mLocalVideo);
        }
    }

    private boolean mIsMute = false;

    private void onClickMute() {
        mIsMute = !mIsMute;
        DebugLog.i(TAG, "onClickMute mIsMute:" + mIsMute);
        if (mIsMute) {
            CallManager.getInstance().unMute();
        } else {
            CallManager.getInstance().mute();
        }
    }

    private boolean mIsStopMyVideo = false;

    private void onClickStopVideo() {
        mIsStopMyVideo = !mIsStopMyVideo;
        DebugLog.i(TAG, "onClickStopVideo mIsStopMyVideo " + mIsStopMyVideo);
        CallManager.getInstance().setLocalVideoEnabled(mIsStopMyVideo);
    }


    /**
     * 音量设置
     *
     * @param volumn
     */
    private void setVolumn(int volumn) {
        CallManager.getInstance().setVolumn(volumn);
    }

    /**
     * 免提设置
     *
     * @param isOutterSpeaker
     */
    public void setSpeakerOn(boolean isOutterSpeaker) {
        // 设置是否免提
        Log.i(TAG, "isOutterSpeaker:" + isOutterSpeaker);

        mAudioManager.setSpeakerphoneOn(isOutterSpeaker);
        CallManager.getInstance().setAudioMode(
                isOutterSpeaker ? CallManager.AUDIO_MODE_HANDSFREE : CallManager.AUDIO_MODE_HANDSET);
    }

    public void headsetChanged() {
        Log.i(TAG, "headsetChanged mHeadsetPlugged:" + mHeadsetPlugged + " mBluetoothHeadsetPlugged:"
                + mBluetoothHeadsetPlugged);

        boolean plugged = mHeadsetPlugged || mBluetoothHeadsetPlugged;

        if (plugged) {
            setSpeakerOn(false);
        } else {
            setSpeakerOn(true);
        }
    }

    @Override
    public void onBluetoothStateChanged(int state) {
        Log.i(TAG, "onBluetoothStateChanged " + state + " isBluetoothA2dpOn:" + mAudioManager.isBluetoothA2dpOn());
        switch (state) {
            case BluetoothHeadset.STATE_CONNECTED:
                setBluetoothHeadsetState(true);
                break;
            case BluetoothHeadset.STATE_DISCONNECTED:
            case BluetoothHeadset.STATE_CONNECTING:
            case BluetoothHeadset.STATE_DISCONNECTING:
                setBluetoothHeadsetState(false);
                break;
        }
    }

    @Override
    public void onHeadsetChanged(boolean plugged) {
        Log.i(TAG, "onHeadsetChanged plugged " + plugged);
        mHeadsetPlugged = plugged;
        headsetChanged();
    }

    private void setBluetoothHeadsetState(boolean plugged) {
        if (mBluetoothHeadsetPlugged == plugged) {
            return;
        }
        mBluetoothHeadsetPlugged = plugged;
        headsetChanged();
    }
}
