package com.xrq.tv.utils;

import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import com.xrq.tv.application.App;
import com.xrq.tv.down.ThreadPool;

import static java.lang.Thread.sleep;

public class NetWorkSpeedUtils {

    private Handler mHandler;

    private long lastTotalRxBytes = 0;
    private boolean runNet = true;

    public NetWorkSpeedUtils( Handler mHandler) {
        this.mHandler = mHandler;
    }


    public void startShowNetSpeed() {
        lastTotalRxBytes = getTotalRxBytes();
        ThreadPool.getInstance().execute(new NetRunnable());
    }

    private long getTotalRxBytes() {
        return TrafficStats.getUidRxBytes(App.getApp().getApplicationInfo().uid);
    }

    private void showNetSpeed() {
        long nowTotalRxBytes = getTotalRxBytes();
        long speed = (nowTotalRxBytes - lastTotalRxBytes) *2 ;
        lastTotalRxBytes = nowTotalRxBytes;
        Message msg = mHandler.obtainMessage();
        msg.what = 100;
        msg.obj = speed;
        mHandler.sendMessage(msg);//更新界面
    }

    public class NetRunnable implements Runnable {
        @Override
        public void run() {
            while (runNet) {
                try {
                    sleep(500);
                    showNetSpeed();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stope() {
        runNet = false;
    }
}
