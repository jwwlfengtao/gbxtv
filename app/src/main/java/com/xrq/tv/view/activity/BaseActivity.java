package com.xrq.tv.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.Toast;
import com.xrq.tv.application.App;
import com.xrq.tv.view.dialog.LoadingDialog;
import com.xrq.tv.view.dialog.VerifyDialog;

/**
 * @author 765773123
 * @date 2018/12/21
 * 基础Activity
 */
public class BaseActivity extends Activity {
    protected VerifyDialog.Builder builder;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void showProess() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog.Builder(this)
                    .creat();
            try {
                if (loadingDialog != null) {
                    loadingDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    protected void hideProess() {
        if (loadingDialog != null) {
            try {
                loadingDialog.hide();
                loadingDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
                loadingDialog = null;
            }
        }
    }

    protected void toast(String msg) {
        if (msg != null) {
            Toast.makeText(App.getApp(), msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        //实现Home键效果
        //super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了
//        moveTaskToBack(true);

        Intent i= new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }




    @Override
    protected void onDestroy() {
        hideProess();
        super.onDestroy();
    }
}
