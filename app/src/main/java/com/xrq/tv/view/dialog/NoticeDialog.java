package com.xrq.tv.view.dialog;

import android.app.Dialog;
import android.content.Context;
;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import android.widget.Button;

import android.widget.TextView;

import com.xrq.tv.R;
import com.xrq.tv.utils.Log;


public class NoticeDialog {
    public static class Builder {
        Context context;
        OnclickListener onclickListener;
        String inputContent = "";

        boolean isOutTouch=true;
        private String text_notice;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setOnclickListener(OnclickListener onclickListener) {
            this.onclickListener = onclickListener;
            return this;
        }

        public Builder setOutTouch(boolean outTouch) {
            isOutTouch = outTouch;
            return this;
        }

        public Builder setNotice(String notice) {
            this.text_notice = notice;
            return this;
        }

        public Dialog create() {
            if (context == null) {
                return null;
            }
            final Dialog dialog = new Dialog(context, R.style.dialog);
            View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_notice_layout, null);
            Button btn_sure = contentView.findViewById(R.id.btn_sure);
            TextView text_describe = contentView.findViewById(R.id.text_describe);
            text_describe.setText(text_notice);
            btn_sure .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onclickListener != null) {
                        onclickListener.onClick();
                    }
                    Log.iii("TAG", "点击了确定按钮");
                    dialog.dismiss();
                }
            });
            //其他设置
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //设置没有标题
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setContentView(contentView);//设置内容
            dialog.setCanceledOnTouchOutside(isOutTouch);//设置Dialog以外是否可触摸
            return dialog;
        }
    }

    /**
     * 点击绑定监听
     */
    public interface OnclickListener {
        /**
         * 确定，是否选中checkBox
         */
        void onClick();

        /**
         * 取消
         */
        void cancel();
    }
}
