package yealink.sample;

import android.bluetooth.BluetoothHeadset;
import android.util.Log;

import java.util.ArrayList;

/**
 * 负责通知蓝牙耳机插入状态变化
 * 
 * @author liurs
 *
 */
public class BluetoothNotifier {
    private static final String TAG = BluetoothNotifier.class.getSimpleName();

    private static BluetoothNotifier sInstance;
    
    private ArrayList<BluetoothListener> mLsnrs = new ArrayList<BluetoothListener>();

    private boolean mFirstTime = true;

    private int mState;
    
    public synchronized static BluetoothNotifier getInstance(){
        if(sInstance == null){
            sInstance = new BluetoothNotifier();
        }
        return sInstance;
    }
    
    /**
     * 监听器
     */
    public static interface BluetoothListener {
        /**
         * @see BluetoothHeadset#STATE_DISCONNECTED ...
         * @param state
         */
        void onBluetoothStateChanged(int state);
    }
    
    /**
     * 注册监听器
     * 
     * @param lsnr
     */
    public synchronized void registerListener(BluetoothListener lsnr){
        if(!mLsnrs.contains(lsnr)){
            mLsnrs.add(lsnr);
        }
        
        Log.i(TAG, "registerListener size " + mLsnrs.size());
    }
    
    /**
     * 反注册监听器
     * 
     * @param lsnr
     */
    public synchronized void unregisterListener(BluetoothListener lsnr){
        mLsnrs.remove(lsnr);
        Log.i(TAG, "unregisterListener size " + mLsnrs.size());
    }
    
    /**
     * 更新耳机状态，由广播监听器来调用
     * 
     * @param 、
     */
    public synchronized void updateState(int state){
        if(mFirstTime){
            mFirstTime = false;
            mState = state;
            notifyState(mState);
        }
        
        if(mState != state){
            mState = state;
            notifyState(mState);
        }
    }

    private void notifyState(int state) {
        for(BluetoothListener lsnr : mLsnrs){
            lsnr.onBluetoothStateChanged(state);
        }
    }
}
