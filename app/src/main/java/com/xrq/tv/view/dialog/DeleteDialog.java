package com.xrq.tv.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xrq.tv.R;

/**
 * @author 765773123
 * @date 2018/12/3
 * 对话确认按钮
 */
public class DeleteDialog {
    public static class Builder {
        Context context;
        OnclickListener onclickListener;
        String inputContent = "";
        boolean isOutTouch;
        private String text_describe;
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


        public Builder setDescribe(String describe) {
            this.text_describe = describe;
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
            View contentView = LayoutInflater.from(context).inflate(R.layout.delete_dialog_layout, null);
            Button btn_sure = contentView.findViewById(R.id.btn_sure);
            Button btn_cancel = contentView.findViewById(R.id.btn_cancel);
            LinearLayout ll_notice = contentView.findViewById(R.id.ll_notice);
            CheckBox checkbox = contentView.findViewById(R.id.checkbox);
            TextView text_describe = contentView.findViewById(R.id.text_describe);
            TextView text_notice = contentView.findViewById(R.id.text_notice);
            RelativeLayout rl_checkbox = contentView.findViewById(R.id.rl_checkbox);
            ImageView image_notice = contentView.findViewById(R.id.image_notice);
            btn_sure.requestFocus();
            rl_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkbox.setChecked(!checkbox.isChecked());
                }
            });
            btn_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onclickListener != null) {
                        onclickListener.onClick(checkbox.isChecked());
                    }
                    dialog.dismiss();
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onclickListener != null) {
                        onclickListener.cancel();
                    }
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
        void onClick(boolean checked);

        /**
         * 取消
         */
        void cancel();
    }
}
