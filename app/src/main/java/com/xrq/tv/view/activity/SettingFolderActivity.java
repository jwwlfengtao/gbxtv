package com.xrq.tv.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xrq.tv.utils.DataModel;
import com.xrq.tv.R;
import com.xrq.tv.utils.Log;

import java.io.File;

/**
 * @author 765773123
 * @date 2018/12/1
 * 设置文件路径
 */
public class SettingFolderActivity extends BaseActivity implements View.OnClickListener {
    private Button choosePath;
    private Button sure;
    private Button cancel;
    private TextView text_path;
    //光标下标，竖向
    private int selectVerIndex = 1;
    //光标下标，横向
    private int selectOrIndex = 0;
    private LinearLayout ll_0;
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_folder);
        choosePath = findViewById(R.id.choosePath);
        sure = findViewById(R.id.sure);
        cancel = findViewById(R.id.cancel);
        text_path = findViewById(R.id.text_path);
        ll_0 = findViewById(R.id.ll_0);
//        choosePath.setOnClickListener(this);
//        sure.setOnClickListener(this);
//        cancel.setOnClickListener(this);
        setSelectedBackGround();
        text_path.setText(DataModel.getInstance().getStorePath());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choosePath:
                Intent intent = new Intent(SettingFolderActivity.this, ChooseFolderActivity.class);
                intent.putExtra("type", "choosePath");
                startActivityForResult(intent, 1);
                overridePendingTransition(0, 0);
                break;
            case R.id.sure:
                DataModel.getInstance().saveStorePath(text_path.getText().toString());
                finish();
                overridePendingTransition(0, 0);
                break;
            case R.id.cancel:
                finish();
                overridePendingTransition(0, 0);
                break;
            default:
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.iii("TAG", "进入设置路径界面");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //模拟器测试时键盘中的的Enter键，模拟ok键（推荐TV开发中使用蓝叠模拟器）
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                oKOnclick();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (selectVerIndex < 2) {
                    selectVerIndex++;
                    setSelectedBackGround();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (selectVerIndex > 0) {
                    selectVerIndex--;
                    setSelectedBackGround();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (selectVerIndex == 2) {
                    selectOrIndex = 1;
                    setSelectedBackGround();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (selectVerIndex == 2) {
                    selectOrIndex = 0;
                    setSelectedBackGround();
                }
                break;
            case KeyEvent.KEYCODE_BACK://返回键
                finish();
                overridePendingTransition(0, 0);
                break;
            default:
        }
        return super.onKeyDown(keyCode, event);
    }

    private void oKOnclick() {
        switch (selectVerIndex) {
            case 0:
                finish();
                overridePendingTransition(0, 0);
                break;
            case 1:
                Intent intent = new Intent(SettingFolderActivity.this, ChooseFolderActivity.class);
                intent.putExtra("type", "choosePath");
                startActivityForResult(intent, 1);
                overridePendingTransition(0, 0);
                break;
            case 2:
                if (selectOrIndex == 0) {
                    File file=new File(text_path.getText().toString());
                    if (!file.exists()){
                        file.mkdir();
                    }
                    DataModel.getInstance().saveStorePath(text_path.getText().toString());
                    finish();
                    overridePendingTransition(0, 0);
                } else if (selectOrIndex == 1) {
                    finish();
                    overridePendingTransition(0, 0);
                }
                break;
            default:
        }
    }

    public void setSelectedBackGround() {
        switch (selectVerIndex) {
            case 0:
                ll_0.setBackground(this.getResources().getDrawable(R.drawable.item_selected));
                choosePath.setBackground(this.getResources().getDrawable(R.drawable.button_unselected));
                sure.setBackground(this.getResources().getDrawable(R.drawable.button_unselected));
                cancel.setBackground(this.getResources().getDrawable(R.drawable.button_unselected));
                Log.iii("TAG", "焦点在返回按钮");
                break;
            case 1:
                ll_0.setBackground(null);
                choosePath.setBackground(this.getResources().getDrawable(R.drawable.button_selected1));
                sure.setBackground(this.getResources().getDrawable(R.drawable.button_unselected));
                cancel.setBackground(this.getResources().getDrawable(R.drawable.button_unselected));
                Log.iii("TAG", "焦点在浏览文件按钮");
                break;
            case 2:
                if (selectOrIndex == 0) {
                    ll_0.setBackground(null);
                    choosePath.setBackground(this.getResources().getDrawable(R.drawable.button_unselected));
                    sure.setBackground(this.getResources().getDrawable(R.drawable.button_selected1));
                    cancel.setBackground(this.getResources().getDrawable(R.drawable.button_unselected));
                } else if (selectOrIndex == 1) {
                    ll_0.setBackground(null);
                    choosePath.setBackground(this.getResources().getDrawable(R.drawable.button_unselected));
                    sure.setBackground(this.getResources().getDrawable(R.drawable.button_unselected));
                    cancel.setBackground(this.getResources().getDrawable(R.drawable.button_selected1));
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            text_path.setText(data.getStringExtra("path"));
        }
    }
}
