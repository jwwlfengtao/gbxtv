package com.xrq.tv.utils;

import android.widget.Toast;

import com.xrq.tv.application.App;

public class ToasUtil {
    public static void showShortToast(String msg) {
        if (msg != null && !"".equals(msg)) {
            Toast.makeText(App.getApp(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showLongToast(String msg) {
        if (msg != null && !"".equals(msg)) {
            Toast.makeText(App.getApp(), msg, Toast.LENGTH_LONG).show();
        }
    }
}
