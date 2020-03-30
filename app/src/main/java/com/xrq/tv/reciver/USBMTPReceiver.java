package com.xrq.tv.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xrq.tv.utils.Log;

public class USBMTPReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.iii("TAG", "action === " + intent.getAction());
        if (intent.getAction().equals("android.intent.action.MEDIA_MOUNTED")) {//U盘插入

            String path = intent.getDataString();
            String pathString = path.split("file://")[1];//U盘路径
            Log.iii("TAG", "U盘已连接" + pathString);

//            insertUSB(pathString);

        } else if (intent.getAction().equals("android.intent.action.MEDIA_REMOVED")) {//U盘拔出
            Log.iii("TAG", "U盘已拔出");
//            removeUSB();
        }

    }
}
