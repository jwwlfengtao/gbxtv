package com.xrq.tv.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xrq.tv.R;
import com.xrq.tv.bean.FileBean;
import com.xrq.tv.constant.Constant;
import com.xrq.tv.down.ThreadPool;
import com.xrq.tv.utils.DataModel;
import com.xrq.tv.utils.Log;
import com.xrq.tv.view.adapter.FolderAdapter;
import com.xrq.tv.view.dialog.DeleteFileDialog;

import java.io.File;
import java.util.ArrayList;

/**
 * @author 765773123
 * @date 2018/12/1
 * 选择文件夹
 */
public class ChooseFolderActivity extends BaseActivity {
    private RecyclerView tv_RecyclerView;
    private TextView path;
    private ArrayList<FileBean> folderList = new ArrayList<>();
    private FolderAdapter folderAdapter;
    //最外层路径
    private  String BAESE_PATH = DataModel.getInstance().getBasePath()+"/";
    private String type = "";
    private int hoCount = 7;
    private LinearLayout ll_0;
    private LinearLayout ll_button;
    //列表是拥有焦点,0:默认拥有焦点，1:焦点在设置,2:焦点既不在列表，又不在设置
    private int recyclerViewHaveFoucse = 0;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0x123:
                    folderAdapter.notifyDataSetChanged();
                    if (folderList.size() == 0) {
                        recyclerViewHaveFoucse = 2;
                    }
                    break;
                default:
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_folder);
        //type="manage",文件管理;type="choosePath"，选择地址;
        type = getIntent().getStringExtra("type");
        tv_RecyclerView = findViewById(R.id.tv_RecyclerView);
        path = findViewById(R.id.path);
        ll_0 = findViewById(R.id.ll_0);
        ll_button = findViewById(R.id.ll_button);
        if ("choosePath".equals(type)) {
            ll_button.setVisibility(View.VISIBLE);
        } else if ("manage".equals(type)) {
            ll_button.setVisibility(View.GONE);
        }
        path.setText("路径:" + BAESE_PATH);
        String sDStateString = Environment.getExternalStorageState();
        folderAdapter = new FolderAdapter(folderList, hoCount);
        GridLayoutManager manager = new GridLayoutManager(ChooseFolderActivity.this, hoCount);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        tv_RecyclerView.setLayoutManager(manager);
        tv_RecyclerView.setAdapter(folderAdapter);
        setFoucseBackGround();
        ThreadPool.getInstance().execute(new FolderRunnable(BAESE_PATH, handler));
        folderAdapter.setFoucsListener(new FolderAdapter.FoucsListener() {
            @Override
            public void outFocs() {
                recyclerViewHaveFoucse = 1;
                setFoucseBackGround();
            }
        });
        folderAdapter.setSelectedListener(new FolderAdapter.SelectedListener() {
            @Override
            public void selectListner(int positon) {
                tv_RecyclerView.scrollToPosition(positon);
            }
        });
        folderAdapter.setOnclickListener(new FolderAdapter.ItemOnclickListener() {
            @Override
            public void enterClick(int position) {
                BAESE_PATH = BAESE_PATH + folderList.get(position).getFileName() + "/";
                path.setText("路径:" + BAESE_PATH);
                ThreadPool.getInstance().execute(new FolderRunnable(BAESE_PATH, handler));
                Log.iii("TAG", "进入子界面");
            }

            @Override
            public void fileClick(int position) {
                Log.iii("TAG", "具体文件操作");
                Dialog dialog = new DeleteFileDialog.Builder(ChooseFolderActivity.this)
                        .setOnclickListener(new DeleteFileDialog.OnclickListener() {
                            @Override
                            public void onClick() {
                                String fileName = BAESE_PATH + folderList.get(position).getFileName();
                                File file = new File(fileName);
                                if (file.exists()) {
                                    boolean b = file.delete();
                                    if (b) {
                                        Log.iii("TAG", "删除文件成功--" + fileName);
                                        ThreadPool.getInstance().execute(new FolderRunnable(BAESE_PATH, handler));
                                    } else {
                                        Log.iii("TAG", "删除文件失败--" + fileName);
                                    }
                                } else {
                                    Log.iii("TAG", "文件不存在--" + fileName);
                                }
                            }

                            @Override
                            public void cancel() {

                            }
                        }).create();
                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.iii("TAG", "进入选择路径界面");
    }

    /**
     * 读取文件
     */
    class FolderRunnable implements Runnable {
        private Handler handler;
        String path;

        public FolderRunnable(String path, Handler handler) {
            this.handler = handler;
            this.path = path;
        }

        @Override
        public void run() {
            if (!"".equals(path)) {
                readFolder(path, handler);
            }
        }
    }

    private void readFolder(String path, Handler handler) {
        Log.iii("TAG", "查询文件路径" + path);
        File file = new File(path);
        if (!file.exists()) {
            Log.iii("TAG", "文件不存在                                                                                                                                     " + path);
            return;
        }
        recyclerViewHaveFoucse = 0;
        folderAdapter.resetPosition();
        folderList.clear();
        File[] filesList = file.listFiles();
        if (filesList == null || filesList.length == 0) {
            handler.sendEmptyMessage(0x123);
            return;
        }
        for (File currentFile : filesList) {
            int currentIcon;
            String fileName = currentFile.getName();
            Log.iii("TAG", "fileName" + fileName);
            if ("choosePath".equals(type)) {
                //判断是一个文件夹还是一个文件
                if (currentFile.isDirectory()) {
                    currentIcon = R.drawable.filesystem_icon_folder;
                    FileBean fileBean = new FileBean();
                    fileBean.setPicId(currentIcon);
                    fileBean.setFileName(fileName);
                    folderList.add(fileBean);
                }
            } else if ("manage".equals(type)) {
                //判断是一个文件夹还是一个文件
                if (currentFile.isDirectory()) {
                    currentIcon = R.drawable.filesystem_icon_folder;
                } else {
                    //取得文件名
                    //根据文件名来判断文件类型，设置不同的图标
                    //图片
                    if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingImage))) {
                        currentIcon = R.drawable.filesystem_grid_icon_photo;
                        //压缩包
                    } else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingPackage))) {
                        currentIcon = R.drawable.filesystem_icon_rar;
                        //语音
                    } else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingAudio))) {
                        currentIcon = R.drawable.filesystem_grid_icon_music;
                        //视频
                    } else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingVideo))) {
                        currentIcon = R.drawable.filesystem_grid_icon_movie;
                        //文字
                    } else if (checkEndsWithInStringArray(fileName, getResources().getStringArray(R.array.fileEndingTxt))) {
                        currentIcon = R.drawable.filesystem_grid_icon_text;
                        //未知类型
                    } else {
                        //未知类型
                        currentIcon = R.drawable.filesystem_icon_default;
                    }
                }
                FileBean fileBean = new FileBean();
                fileBean.setPicId(currentIcon);
                fileBean.setFileName(fileName);
                folderList.add(fileBean);
            }
            if (folderList.size() > 0) {
                folderList.get(0).setSelected(true);
            }
            handler.sendEmptyMessage(0x123);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.iii("TAG", "按下了遥控板" + keyCode);
        switch (keyCode) {
            //模拟器测试时键盘中的的Enter键，模拟ok键（推荐TV开发中使用蓝叠模拟器）
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                Log.iii("TAG", "你按下中间键");
                oKOnclick();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                Log.iii("TAG", "你按下下方向键recyclerViewHaveFoucse" + recyclerViewHaveFoucse);
                if (recyclerViewHaveFoucse == 0) {
                    folderAdapter.selectDown();
                } else if (recyclerViewHaveFoucse == 1) {
                    if (folderList.size() > 0) {
                        //默认第一个item为焦点
                        recyclerViewHaveFoucse = 0;
                        setFoucseBackGround();
                        folderAdapter.resettFouce();
                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (recyclerViewHaveFoucse == 0) {
                    folderAdapter.selectLeft();
                }
                Log.iii("TAG", "你按下左方向键");
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.iii("TAG", "你按下右方向键recyclerViewHaveFoucse" + recyclerViewHaveFoucse);
                if (recyclerViewHaveFoucse == 0) {
                    folderAdapter.selectRight();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (recyclerViewHaveFoucse == 0) {
                    folderAdapter.selectUp();
                }
                Log.iii("TAG", "你按下上方向键");
                break;
            case KeyEvent.KEYCODE_BACK://返回键
                String bas = "/mnt/media_rw/sda1/";
                StringBuffer stringBuffer = new StringBuffer(BAESE_PATH);
                if (stringBuffer.length() > bas.length()) {
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                    int dex = stringBuffer.lastIndexOf("/");
                    stringBuffer.delete(dex + 1, stringBuffer.length());
                    BAESE_PATH = stringBuffer.toString();
                    path.setText("路径:" + BAESE_PATH);
                    ThreadPool.getInstance().execute(new FolderRunnable(BAESE_PATH, handler));
                    return false;
                } else {
                    finish();
                    overridePendingTransition(0, 0);
                    return true;
                }
            case KeyEvent.KEYCODE_MENU://菜单键
                if ("choosePath".equals(type)) {
                    Intent intent = new Intent();
                    intent.putExtra("path", BAESE_PATH + Constant.FILE_NAME);
                    Log.iii("TAG", "选择的地址是" + BAESE_PATH + Constant.FILE_NAME);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    overridePendingTransition(0, 0);
                    return true;
                } else {
                    return super.onKeyDown(keyCode, event);
                }
            default:

        }
        return super.onKeyDown(keyCode, event);
    }

    private void oKOnclick() {
        if (recyclerViewHaveFoucse == 0) {
            if ("choosePath".equals(type)) {
                if (!folderList.isEmpty()) {
                    BAESE_PATH = BAESE_PATH + folderList.get(folderAdapter.getLastSelectPostion()).getFileName() + "/";
                    path.setText("路径:" + BAESE_PATH);
                    ThreadPool.getInstance().execute(new FolderRunnable(BAESE_PATH, handler));
                }
                Log.iii("TAG", "进入子界面");
            } else if ("manage".equals(type)) {
                if (!folderList.isEmpty()) {
                    if (folderList.get(folderAdapter.getLastSelectPostion()).getPicId() == R.drawable.filesystem_icon_folder) {
                        BAESE_PATH = BAESE_PATH + folderList.get(folderAdapter.getLastSelectPostion()).getFileName() + "/";
                        path.setText("路径:" + BAESE_PATH);
                        ThreadPool.getInstance().execute(new FolderRunnable(BAESE_PATH, handler));

                        Log.iii("TAG", "进入子界面");
                    } else {
                        //弹出是否删除该文件
                        Dialog dialog = new DeleteFileDialog.Builder(this)
                                .setOnclickListener(new DeleteFileDialog.OnclickListener() {
                                    @Override
                                    public void onClick() {
                                        String fileName = BAESE_PATH + folderList.get(folderAdapter.getLastSelectPostion()).getFileName();
                                        File file = new File(fileName);
                                        if (file.exists()) {
                                            boolean b = file.delete();
                                            if (b) {
                                                Log.iii("TAG", "删除文件成功--" + fileName);
                                                ThreadPool.getInstance().execute(new FolderRunnable(BAESE_PATH, handler));
                                            } else {
                                                Log.iii("TAG", "删除文件失败--" + fileName);
                                            }
                                        } else {
                                            Log.iii("TAG", "文件不存在--" + fileName);
                                        }
                                    }

                                    @Override
                                    public void cancel() {

                                    }
                                })
                                .create();
                        dialog.show();
                    }
                }
            }
        } else if (recyclerViewHaveFoucse == 1) {
            finish();
            overridePendingTransition(0, 0);
        } else if (recyclerViewHaveFoucse == 2) {

        }

    }

    private void setFoucseBackGround() {
        if (recyclerViewHaveFoucse == 0) {
            ll_0.setBackground(null);
        } else if (recyclerViewHaveFoucse == 1) {
            ll_0.setBackground(this.getResources().getDrawable(R.drawable.item_selected));
        } else if (recyclerViewHaveFoucse == 2) {
            ll_0.setBackground(null);
        }
    }

    //通过文件名判断是什么类型的文件
    private boolean checkEndsWithInStringArray(String checkItsEnd, String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.endsWith(aEnd)) {
                return true;
            }
        }
        return false;
    }
}
