package com.xrq.tv.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.xrq.tv.application.App;

/**
 * 版本相关工具
 */
public class VersionUtils {

    //获取versioncode
    public static int getVersionCode() {
        try {
            PackageInfo packageInfo = App.getApp().getPackageManager()
                    .getPackageInfo(App.getApp().getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //获取verisionName
    public static String getVersionName() {
        try {
            PackageInfo packageInfo = App.getApp().getPackageManager()
                    .getPackageInfo(App.getApp().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
