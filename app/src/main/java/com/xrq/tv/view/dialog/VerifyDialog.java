package com.xrq.tv.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xrq.tv.R;
import com.xrq.tv.utils.Log;
import com.xrq.tv.utils.ToasUtil;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * @author 765773123
 * @date 2018/11/27
 * 验证码Dialog
 */
public class VerifyDialog {
    public static class Builder {
        Context context;
        OnclickListener onclickListener;
        String inputContent = "";
        boolean isOutTouch;

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

        public Dialog create() {
            if (context == null) {
                return null;
            }
            final Dialog dialog = new Dialog(context, R.style.dialog);
            View contentView = LayoutInflater.from(context).inflate(R.layout.verify_dialog_layout, null);
            Button btn_sure = contentView.findViewById(R.id.btn_sure);
            LinearLayout ll_code = contentView.findViewById(R.id.ll_code);
            TextView v1 = contentView.findViewById(R.id.v1);
            TextView v2 = contentView.findViewById(R.id.v2);
            TextView v3 = contentView.findViewById(R.id.v3);
            TextView v4 = contentView.findViewById(R.id.v4);
            TextView v5 = contentView.findViewById(R.id.v5);
            TextView v6 = contentView.findViewById(R.id.v6);
            TextView v7 = contentView.findViewById(R.id.v7);
            TextView v8 = contentView.findViewById(R.id.v8);
            TextView text_mac = contentView.findViewById(R.id.text_mac);
            EditText edit = contentView.findViewById(R.id.edit);
            TextView[] textViews = {v1, v2, v3, v4, v5, v6, v7, v8};
            text_mac.setText(getNewMac());
            ll_code.setFocusable(true);
            edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    inputContent = s.toString();
                    for (int i = 0; i < 8; i++) {
                        if (i < s.length()) {
                            textViews[i].setText(String.valueOf(s.charAt(i)));
                        } else {
                            textViews[i].setText("");
                        }
                    }
                    if (s.length() == 8) {
                        btn_sure.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(v, 0);
                    } else {
                        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        btn_sure.requestFocus();
                    }
                }
            });
            btn_sure.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        edit.requestFocus();
                    }
                }
            });
            btn_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onclickListener != null) {
                        if (inputContent.length() != 8) {
                            ToasUtil.showLongToast("请输入正确的验证码！");
                            return;
                        }
                        onclickListener.onClick(inputContent);
                    }
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
         * 点击绑定
         *
         * @param msg 激活码
         */
        void onClick(String msg);
    }

    /**
     * 通过网络接口取
     *
     * @return
     */
    private static String getNewMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("eth0")) {
                    continue;
                }

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
