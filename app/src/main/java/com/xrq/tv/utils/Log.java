package com.xrq.tv.utils;

import com.xrq.tv.BuildConfig;
import com.xrq.tv.constant.Constant;

/**
 * @author 765773123
 * @date 2019/1/2
 * 打印日志
 */
public class Log {
    public static void iii(String tag, String msg) {
        if(BuildConfig.BUILD_TYPE.equals("debug"))
            android.util.Log.i(tag, msg);
    }
}
