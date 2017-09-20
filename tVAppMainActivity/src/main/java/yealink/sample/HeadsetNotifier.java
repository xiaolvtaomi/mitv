package yealink.sample;


import android.util.Log;

import java.util.ArrayList;

/**
 * 负责通知耳机插入状态变化
 * 
 * @author liurs
 *
 */
public class HeadsetNotifier {
    private static final String TAG = HeadsetNotifier.class.getSimpleName();

    private static HeadsetNotifier sInstance;
    
    private ArrayList<HeadsetListener> mLsnrs = new ArrayList<HeadsetListener>();

    private boolean mFirstTime = true;

    private boolean mPlugged;
    
    public synchronized static HeadsetNotifier getInstance(){
        if(sInstance == null){
            sInstance = new HeadsetNotifier();
        }
        return sInstance;
    }
    
    /**
     * 监听器
     */
    public static interface HeadsetListener {
        void onHeadsetChanged(boolean plugged);
    }
    
    /**
     * 注册监听器
     * 
     * @param lsnr
     */
    public synchronized void registerListener(HeadsetListener lsnr){
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
    public synchronized void unregisterListener(HeadsetListener lsnr){
        mLsnrs.remove(lsnr);
        Log.i(TAG, "unregisterListener size " + mLsnrs.size());
    }
    
    /**
     * 更新耳机状态，由广播监听器来调用
     * 
     * @param plugged
     */
    public synchronized void updateState(boolean plugged){
        if(mFirstTime){
            mFirstTime = false;
            mPlugged = plugged;
        }
        
        if(mPlugged != plugged){
            mPlugged = plugged;
            notifyState(mPlugged);
        }
    }

    private void notifyState(boolean plugged) {
        for(HeadsetListener lsnr : mLsnrs){
            lsnr.onHeadsetChanged(plugged);
        }
    }
}
