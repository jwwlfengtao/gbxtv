package com.xrq.tv.view.activity;


import android.content.Intent;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.xrq.tv.utils.DataModel;
import com.xrq.tv.R;
import com.xrq.tv.utils.Log;

/**
 * @author 765773123
 * @date 2018/12/21
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_0;
    private LinearLayout ll_1;
    private LinearLayout ll_2;
    private LinearLayout ll_3;
    private TextView path;
    private Switch mSwitch;
    private boolean isOn = true;
    //光标下标
    private int selectIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ll_0 = findViewById(R.id.ll_0);
        ll_1 = findViewById(R.id.ll_1);
        ll_2 = findViewById(R.id.ll_2);
        ll_3 = findViewById(R.id.ll_3);
        path = findViewById(R.id.path);
        mSwitch = findViewById(R.id.mSwitch);
        setSelectedBackGround();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_2:
                Intent intent = new Intent(SettingActivity.this, SettingFolderActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(0, 0);
                break;
            case R.id.ll_3:
                Log.iii("TAG", "点击了文件管理");
                Intent intent1 = new Intent(SettingActivity.this, ChooseFolderActivity.class);
                intent1.putExtra("type", "manage");
                startActivity(intent1);
                overridePendingTransition(0, 0);
                break;
            default:
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //模拟器测试时键盘中的的Enter键，模拟ok键（推荐TV开发中使用蓝叠模拟器）
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                Log.iii("TAG", "你按下中间键");
                if (selectIndex == 1) {
                    isOn = !isOn;
                }
                oKOnclick();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (selectIndex < 3) {
                    selectIndex++;
                    setSelectedBackGround();
                }
                Log.iii("TAG", "你按下下方向键");
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (selectIndex > 0) {
                    selectIndex--;
                    setSelectedBackGround();
                }
                Log.iii("TAG", "你按下上方向键");
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                break;
            case KeyEvent.KEYCODE_BACK://返回键
                finish();
                overridePendingTransition(0, 0);
                break;
            default:
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.iii("TAG", "进入设置界面");
        path.setText("设置默认下载路径(" + DataModel.getInstance().getStorePath() + ")");
    }

    public void setSelectedBackGround() {
        switch (selectIndex) {
            case 0:
                ll_0.setBackground(this.getResources().getDrawable(R.drawable.item_selected));
                ll_1.setBackground(null);
                ll_2.setBackground(null);
                ll_3.setBackground(null);
                Log.iii("TAG", "焦点在返回");
                break;
            case 1:
                ll_1.setBackground(this.getResources().getDrawable(R.drawable.item_bg));
                ll_0.setBackground(null);
                ll_2.setBackground(null);
                ll_3.setBackground(null);
                Log.iii("TAG", "焦点在悬浮框");
                break;
            case 2:
                ll_2.setBackground(this.getResources().getDrawable(R.drawable.item_bg));
                ll_0.setBackground(null);
                ll_1.setBackground(null);
                ll_3.setBackground(null);
                Log.iii("TAG", "焦点在文件路径管理");
                break;
            case 3:
                ll_3.setBackground(this.getResources().getDrawable(R.drawable.item_bg));
                ll_0.setBackground(null);
                ll_1.setBackground(null);
                ll_2.setBackground(null);
                Log.iii("TAG", "焦点在文件管理");
                break;
            default:
        }
    }

    private void oKOnclick() {
        switch (selectIndex) {
            case 0:
                Log.iii("TAG", "点击了返回");
                finish();
                overridePendingTransition(0, 0);
                break;
            case 1:
                if (isOn) {
                    mSwitch.setChecked(true);
                    Log.iii("TAG", "悬浮框开");
                } else {
                    mSwitch.setChecked(false);
                    Log.iii("TAG", "悬浮框关");
                }
                break;
            case 2:
                Log.iii("TAG", "点击了文件路径管理");
                Intent intent = new Intent(SettingActivity.this, SettingFolderActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(0, 0);
                break;
            case 3:
                Log.iii("TAG", "点击了文件管理");
                Intent intent1 = new Intent(SettingActivity.this, ChooseFolderActivity.class);
                intent1.putExtra("type", "manage");
                startActivity(intent1);
                overridePendingTransition(0, 0);
                break;
            default:
        }
    }
}


