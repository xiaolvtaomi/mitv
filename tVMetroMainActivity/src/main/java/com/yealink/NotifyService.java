package com.yealink;

import android.app.Service;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.yealink.lib.common.wrapper.CallManager;
import com.yealink.lib.common.wrapper.CallSession;
import com.yealink.lib.common.wrapper.CalllogManager;
import com.yealink.lib.common.wrapper.NativeInit;
import com.yealink.lib.common.wrapper.SettingsManager;
import com.yealink.lib.debug.DebugLog;
import com.yealink.sample.BluetoothNotifier;
import com.yealink.sample.HeadsetNotifier;
import com.yealink.sample.TalkingActivity;


/**
 * 负责后台通知栏提示
 *
 * @author liurs
 */
public class NotifyService extends Service implements NativeInit.NativeInitListener,
        CalllogManager.CalllogListener, Callback, SettingsManager.AccountStateListener {
    private static final String TAG = NotifyService.class.getSimpleName();

    private CallManager.CallListener mCallListener = new CallManager.CallAdapter() {
        @Override
        public void onIncomingCall(CallSession session) {
            DebugLog.i(TAG, "onIncomingCall " + session.remoteDisplayName);
            startCallActivityIfNeeded();

        }
        @Override
        public void onCallFinished(CallSession session, int errorCode, int extraCode) {
        }
        @Override
        public void onCallFailed(CallSession session, int errorCode, int extraCode) {

        }
        @Override
        public void onCallEstablished(CallSession session) {
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.i(TAG, "onReceive " + action);

            if(Intent.ACTION_HEADSET_PLUG.equals(action)){
                HeadsetNotifier.getInstance().updateState(intent.getIntExtra("state", 0) == 1);
            } else if(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action)){
                int state = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, BluetoothHeadset.STATE_DISCONNECTED);
                BluetoothNotifier.getInstance().updateState(state);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        DebugLog.i(TAG, "onCreate");

        // listen init
        NativeInit.registerListener(this);

        // listen calls
        CallManager.getInstance().registerCallListener(mCallListener);

        // listen account
        SettingsManager.getInstance().registerAccountListener(this);

        // listen unread
        CalllogManager.getInstance().registerListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
        filter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DebugLog.i(TAG, "onStartCommand " + startId);

        if(NativeInit.isInited()){
            NativeInit.unregisterListener(this);
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        DebugLog.i(TAG, "onDestroy");
        CallManager.getInstance().unregisterCallListener(mCallListener);
        SettingsManager.getInstance().unregisterAccountListener(this);
    }

    /**
     * 如果主界面未启动，启动主界面
     */
    private void startCallActivityIfNeeded() {
        if(!TalkingActivity.isShowing()){
            DebugLog.i(TAG, "startCallActivity from service");
            Intent it = new Intent(this, TalkingActivity.class);
            it.putExtra(TalkingActivity.EXTRA_IS_INCOMING,true);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
        }
    }

    /**
     * 启动服务
     *
     * @param context
     */
    public static void start(Context context){
        Intent it = new Intent(context, NotifyService.class);
        context.startService(it);
    }


    @Override
    public void onInitFinish() {

    }

    @Override
    public void onCalllogChanged() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            default:
                break;
        }
        return false;
    }

    @Override
    public void onAccountStateChanged(int account, int state, int protocol) {

    }

    @Override
    public void onNativeError(int errorCode) {
        // do nothing
    }
}
