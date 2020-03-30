package com.xrq.tv.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageView;


import com.xrq.tv.R;
import com.xrq.tv.down.ThreadPool;


/**
 * 加载Loading
 */
public class LoadingDialog {
    public static class Builder {
        private Context context;
        private int themid = 0;
        private String describle = "";
        private boolean isOutTouch = false;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setDescrible(String describle) {
            this.describle = describle;
            return this;
        }

        public Builder setOutouch(boolean outouch) {
            this.isOutTouch = outouch;
            return this;
        }

        public Dialog creat() {
            if (context == null) {
                new Throwable("Dialog context is null");
                return null;
            }
            final Dialog dialog = new Dialog(context, 0);
            View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_load_layout, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //设置没有标题
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setContentView(contentView);//设置内容
            dialog.setCanceledOnTouchOutside(true);//设置Dialog以外是否可触摸
            return dialog;
        }
    }
}
