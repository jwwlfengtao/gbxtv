
package com.xrq.tv.view.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xrq.tv.BuildConfig;
import com.xrq.tv.R;
import com.xrq.tv.bean.AllTaskBean;
import com.xrq.tv.bean.DateBean;
import com.xrq.tv.bean.ItemBean;
import com.xrq.tv.constant.Constant;
import com.xrq.tv.utils.CheckTaskUtils;
import com.xrq.tv.utils.CheckVipUtils;
import com.xrq.tv.utils.DataModel;
import com.xrq.tv.utils.Log;
import com.xrq.tv.utils.MainUtils;
import com.xrq.tv.utils.SharedPreferencesUtils;
import com.xrq.tv.utils.StringUtils;
import com.xrq.tv.utils.ToasUtil;
import com.xrq.tv.utils.UpgradeUtil;
import com.xrq.tv.utils.VersionUtils;
import com.xrq.tv.view.adapter.DowningAdapter;
import com.xrq.tv.view.adapter.MainAdapter;
import com.xrq.tv.view.dialog.DeleteDialog;
import com.xrq.tv.view.dialog.NoticeDialog;
import com.xrq.tv.view.dialog.VerifyDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import l.r.ProxyServer;


/**
 * @author 765773123
 * @date 2018/11/26
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ProgressBar pb;
    private MainAdapter mainAdapter;
    private List<ItemBean> mainBeanList = new ArrayList<>();
    private RecyclerView tvRecyclerView;
    private LinearLayout ll_set;
    private LinearLayout ll_empty;
    private LinearLayout ll_down_com_content;
    private Button button;
    private TextView text_downCom;
    private TextView text_useMemory;
    private TextView text_toatalMemory;
    private TextView text_VIP_time;
    private TextView text_codeName;
    private TextView text_netState;

    private boolean haveInitList = false;
    /**
     * 所有任务列表
     */
    private List<AllTaskBean.DataBean> allTaskList = new ArrayList<>();


    private String SD_PATH = "";
    private static final String TAG = "MainActivity";
    private Dialog verifyDialog;
    private boolean isStopAll = false;
    private RecyclerView downing_recyclerView;
    private DowningAdapter downingAdapter;
    private String savePath = "";

    private long[] ROM_USAGE = new long[]{0, 0, 0};
    private long lastUpdate = 0;
    private ScheduledExecutorService mService = Executors.newSingleThreadScheduledExecutor();
    private UpgradeUtil upgradeUtil;
    private static final String UPGRADE_URL = BuildConfig.UPGRADE_URL;
    private ProxyServer proxyServer;
    private Runnable RomUsage = new Runnable() {
        @Override
        public void run() {
            if (SD_PATH.equals("")) return;
            if (System.currentTimeMillis() - lastUpdate <= 60 * 1000) return;
            lastUpdate = System.currentTimeMillis();
            StatFs statFs = new StatFs(SD_PATH);
            ROM_USAGE[0] = statFs.getFreeBytes();
            ROM_USAGE[1] = statFs.getTotalBytes();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    text_toatalMemory.setText("总共" + getInternalMemorySize(MainActivity.this));
                    text_useMemory.setText("已用" + getAvailableInternalMemorySize(MainActivity.this));
                }
            });

        }
    };
    /**
     * 获取列表数据及下载情况更新
     */
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            hideProess();
            switch (msg.what) {
                case 0x8888:
                    upgradeUtil.alert();
                    break;
                case 0x123:
                    Log.iii("TAG", "获取失败-------->" + msg.obj.toString());
                    if ("Token失效".equals(msg.obj.toString())) {
                        registerVip();
                    }
                    break;
                case 0x1234:
                    Log.iii("TAG", "获取数据成功-------->" + msg.obj.toString());
                    //设置数据
                    List<DateBean> dateBeanList = (List<DateBean>) msg.obj;
                    dealWithData(dateBeanList);
                    haveInitList = true;
                    break;
                case 0x12345:
                    Log.iii("TAG", "获取成功,空数据-------->" + msg.obj.toString());
                    break;
                case 0x123456:
                    //更新下载情况
                    AllTaskBean data = (AllTaskBean) msg.obj;
                    allTaskList.clear();
                    allTaskList.addAll(data.getData());
                    showLeft();
                    text_useMemory.setText("已用" + getAvailableInternalMemorySize(MainActivity.this));
                    pb.setProgress(getMemoryProgress());
                    break;
                case 0x1234567:
                    MainUtils.getInstance().getTaskList(handler);
                    break;
                default:
            }
            return false;
        }
    });
    /**
     * 是否认证成功
     */
    private boolean verifySuccessful = false;
    /**
     * 认证相关
     */
    private Handler verifyHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            hideProess();
            switch (msg.what) {
                case 0x123:
                    Log.iii("TAG", "注册失败-------->" + msg.obj.toString());
                    verifySuccessful = false;
                    hideProess();
                    toast(msg.obj.toString());
                    break;
                case 0x1234:
                    toast(msg.obj.toString());
                    Log.iii("TAG", "注册成功-------->" + msg.obj.toString());
                    hideProess();
                    verifySuccessful = true;
                    if (verifyDialog != null) {
                        verifyDialog.dismiss();
                    }
                    //刷新数据
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    break;
                default:
            }
            return false;
        }
    });


    /**
     * 显示网速
     *
     * @param speed 下载速度
     */
    private void showNetSpeed(int speed) {
        if (isStopAll) {
            text_netState.setText("下载速度:0B/S");
        } else {
            try {
                if (allTaskList.size() != 0) {
                    text_netState.setText("下载速度:" + Formatter.formatFileSize(this, speed) + "/S");
                } else {
                    text_netState.setText("无下载任务");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 处理数据
     *
     * @param dateBeanList 从接口请求的数据
     */
    private void dealWithData(List<DateBean> dateBeanList) {
        if (dateBeanList == null) return;
        mainBeanList.clear();
        for (int i = 0; i < dateBeanList.size(); i++) {
            String a = dateBeanList.get(i).get$().getVALUE();
            String b = dateBeanList.get(i).get$().getSTATUS();
            List<ItemBean> itemBeanList = dateBeanList.get(i).getITEM();
            if (itemBeanList != null && !itemBeanList.isEmpty()) {
                itemBeanList.get(0).setDATA(a);
                itemBeanList.get(0).setSTATUS(b);
                if (itemBeanList.size() % 2 != 0) {
                    //末尾占位
                    ItemBean itemBean = new ItemBean();
                    itemBean.setNull(true);
                    itemBeanList.add(itemBean);
                }
                if (itemBeanList.size() >= 2) {
                    //设置为INVISIBLE
                    itemBeanList.get(0).setIvisibleTime(View.VISIBLE);
                    itemBeanList.get(1).setIvisibleTime(View.INVISIBLE);
                }
            }

            mainBeanList.addAll(itemBeanList);
        }
        downing_recyclerView.setAdapter(downingAdapter);
        if (mainBeanList.size() > 0) {
            mainBeanList.get(0).setSelected(true);
        }
        mainAdapter.notifyDataSetChanged();

        handler.sendEmptyMessageDelayed(0x1234567, 4000);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        upgradeUtil = new UpgradeUtil(UPGRADE_URL, this, handler);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50000);
                    upgradeUtil.startCheck();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        setContentView(R.layout.activity_main);
        pb = findViewById(R.id.pb);
        tvRecyclerView = findViewById(R.id.tvRecyclerView);
        ((SimpleItemAnimator) tvRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        ll_set = findViewById(R.id.ll_set);
        ll_empty = findViewById(R.id.ll_empty);
        text_downCom = findViewById(R.id.text_downCom);
        button = findViewById(R.id.button);
        text_codeName = findViewById(R.id.text_codeName);
        text_VIP_time = findViewById(R.id.text_VIP_time);
        text_useMemory = findViewById(R.id.text_useMemory);
        text_toatalMemory = findViewById(R.id.text_toatalMemory);
        ll_down_com_content = findViewById(R.id.ll_down_com_content);
        downing_recyclerView = findViewById(R.id.downing_recyclerView);
        ((SimpleItemAnimator) downing_recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        text_netState = findViewById(R.id.text_netState);

        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .rationale((requestCode, rationale) -> {
                    // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
                    AndPermission.rationaleDialog(this, rationale).show();
                })
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        System.out.println("读写权限onSucceed" + requestCode);
                        if (requestCode == 100) {
                            MainUtils.getInstance().getListData(handler);
                            if (CheckVipUtils.getInstance().isVip()) {
                                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                                Log.iii("TAG", "获取到的存储地址" + path);
                                DataModel.getInstance().saveBasePath(path);
                                if (DataModel.getInstance().getStorePath() == null || "".equals(DataModel.getInstance().getStorePath())) {
//                                            //创建默认下载文件夹
                                    File file = new File(DataModel.getInstance().getBasePath() + "/" + Constant.FILE_NAME);
                                    if (!file.exists()) {
                                        boolean b = file.mkdirs();
                                        if (b) {
                                            DataModel.getInstance().saveStorePath(DataModel.getInstance().getBasePath() + "/" + Constant.FILE_NAME);
                                        } else {

                                        }
                                    } else {
                                        DataModel.getInstance().saveStorePath(DataModel.getInstance().getBasePath() + "/" + Constant.FILE_NAME);
                                    }
                                } else {
                                    //文件路径存在
                                    File file = new File(DataModel.getInstance().getStorePath());
                                    //文件夹不存在则创建文件夹
                                    if (!file.exists()) {
                                        boolean b = file.mkdirs();
                                        if (!b) {
                                            File file1 = new File(DataModel.getInstance().getBasePath() + "/" + Constant.FILE_NAME);
                                            if (!file1.exists()) {
                                                boolean b1 = file1.mkdirs();
                                                if (b1) {
                                                    DataModel.getInstance().saveStorePath(DataModel.getInstance().getBasePath() + "/" + Constant.FILE_NAME);
                                                }
                                            } else {
                                                DataModel.getInstance().saveStorePath(DataModel.getInstance().getBasePath() + "/" + Constant.FILE_NAME);
                                            }
                                        }
                                    }
                                }
                                savePath = DataModel.getInstance().getStorePath();
                                SD_PATH = savePath;
                                Log.iii("TAG", " savePath" + savePath);
                            } else {
                                registerVip();
                            }
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        System.out.println("读写权限权限onFailed");
                        if (requestCode == 100) {

                        }
                    }
                }).start();
        mService.scheduleWithFixedDelay(RomUsage, 1, 1, TimeUnit.SECONDS);
        mainAdapter = new MainAdapter(mainBeanList);
        downingAdapter = new DowningAdapter();
        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 2);
        GridLayoutManager manager1 = new GridLayoutManager(MainActivity.this, 1);
        tvRecyclerView.setLayoutManager(manager);
        tvRecyclerView.setAdapter(mainAdapter);
        downing_recyclerView.setLayoutManager(manager1);
        ll_set.setOnClickListener(this);
        button.setOnClickListener(this);
        if (DataModel.getInstance().getExpireTime() != null) {
            text_VIP_time.setText(StringUtils.getInstance()
                    .setModal(StringUtils.TIME_TYPE_4)
                    .regularization(DataModel.getInstance().getExpireTime(), "T", " ") + "VIP到期");
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        text_codeName.setText("版本:" + VersionUtils.getVersionName() + "   " + df.format(System.currentTimeMillis()));
        mainAdapter.setAddTaskListener(new MainAdapter.AddTaskListener() {
            @Override
            public void add(boolean notify, int position) {
                addTask(notify, position);
            }
        });
        downingAdapter.setDeleteListener(new DowningAdapter.DeleteListener() {
            @Override
            public void delete(String infoHash, String uri, int positon) {
                deleteDownLoading(infoHash, uri, positon);
            }
        });
        text_useMemory.setText("已用" + getAvailableInternalMemorySize(this));
        text_toatalMemory.setText("总共" + getInternalMemorySize(this));
        pb.setProgress(getMemoryProgress());
        //初始化下载路径
        proxyServer = new ProxyServer();
        proxyServer.start(this, savePath);
    }

    /**
     * 注册Vip
     */
    public void registerVip() {
        //没有注册或者注册失效，去注册
        verifyDialog = new VerifyDialog.Builder(MainActivity.this)
                .setOnclickListener(new VerifyDialog.OnclickListener() {
                    @Override
                    public void onClick(String msg) {
                        showProess();
                        CheckVipUtils.getInstance().registerVip(msg, verifyHandler);
                    }
                })
                .create();
        verifyDialog.show();
        verifyDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!verifySuccessful) {
                    finish();
                    overridePendingTransition(0, 0);
                }
            }
        });
    }

    /**
     * 数据处理
     */
    public void showLeft() {
        Log.iii("TAG", "设置左侧数据了" + allTaskList.size());
        //已完成
        List<AllTaskBean.DataBean> haveDownTaskList = new ArrayList<>();
        //正在下载或者等待下载
        List<AllTaskBean.DataBean> noDownTaskList = new ArrayList<>();
        double totalSpeed = 0.0;
        for (int index = 0; index < allTaskList.size(); index++) {
            //下载完成
            if (allTaskList.get(index).getState() == 10) {
                haveDownTaskList.add(allTaskList.get(index));
            } else {
                noDownTaskList.add(allTaskList.get(index));
            }
            totalSpeed = totalSpeed + allTaskList.get(index).getDownloadSpeed();
        }
        showNetSpeed((int) totalSpeed);
        List<ItemBean> itemBeans = new ArrayList<>();
        //显示正在下载
        for (int m = 0; m < noDownTaskList.size(); m++) {
            for (int index = 0; index < mainBeanList.size(); index++) {
                if (noDownTaskList.get(m).getMagnet().equals(mainBeanList.get(index).getURL())) {
                    mainBeanList.get(index).setStateName(noDownTaskList.get(m).getState_cn());
                    mainBeanList.get(index).setState(noDownTaskList.get(m).getState());
                    mainBeanList.get(index).setInfoHash(noDownTaskList.get(m).getInfoHash());
                    mainBeanList.get(index).setProgerss((int) (noDownTaskList.get(m).getProgress() * 100));
                    mainBeanList.get(index).setAdd(true);
                    itemBeans.add(mainBeanList.get(index));
                    mainAdapter.notifyItemChanged(index);
                }
            }
        }
        downingAdapter.initItem(itemBeans);
        if (downingAdapter.getItemCount() > 0) {
            ll_empty.setVisibility(View.GONE);
            downing_recyclerView.setVisibility(View.VISIBLE);
        } else {
            ll_empty.setVisibility(View.VISIBLE);
            downing_recyclerView.setVisibility(View.GONE);
        }

        //显示已完成
        ll_down_com_content.removeAllViews();
        for (int m = haveDownTaskList.size() - 1; m >= 0; m--) {
            for (int index = 0; index < mainBeanList.size(); index++) {
                if (haveDownTaskList.get(m).getMagnet().equals(mainBeanList.get(index).getURL())) {

                    if (ll_down_com_content.getChildCount() < 3) {
                        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.main_down_com_view_layout, null, false);
                        TextView name = view.findViewById(R.id.text_Name);
                        TextView textState = view.findViewById(R.id.text_State);
                        name.setText(mainBeanList.get(index).getZHNAME().get(0));
                        textState.setText("下载完成");
                        text_downCom.setVisibility(View.GONE);
                        ll_down_com_content.addView(view);
                    }
                }
            }
        }
        handler.sendEmptyMessageDelayed(0x1234567, 4000);
    }

    /**
     * 显示内盘不存在提示
     */
    public void showDialog() {
        Dialog dialog = new NoticeDialog.Builder(this)
                .setNotice("内置硬盘不存在，请检查硬盘")
                .setOnclickListener(new NoticeDialog.OnclickListener() {
                    @Override
                    public void onClick() {
                        finish();
                        overridePendingTransition(0, 0);
                    }

                    @Override
                    public void cancel() {

                    }
                })
                .create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        if (!haveInitList) MainUtils.getInstance().getListData(handler);
        String path = DataModel.getInstance().getStorePath();
        //存储地址已变更
        if (savePath == null || !savePath.equals(path)) {
            Log.iii("TAG", "地址已变更，新地址为" + path);
            this.savePath = path;
            SD_PATH = path;
            //初始化下载路径
            if (proxyServer != null) {
                proxyServer.start(this, savePath);
            }

        }
        super.onResume();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_set:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.button:
                if ("暂停所有下载".equals(button.getText().toString())) {
                    button.setText("开始所有下载");
                    isStopAll = true;
                    //全部暂停
                    downingAdapter.notifyDataSetChanged();
                    for (AllTaskBean.DataBean task : allTaskList) {
                        if (task.getState() != 10) {
                            MainUtils.getInstance().stopTask(task.getInfoHash(), handler);
                        }
                    }
                } else {
                    button.setText("暂停所有下载");
                    isStopAll = false;
                    //重新下载
                    for (AllTaskBean.DataBean task : allTaskList) {
                        if (task.getState() != 10) {
                            MainUtils.getInstance().startTask(task.getInfoHash(), handler);
                        }
                    }
                }
                break;
            default:
        }
    }

    /**
     * 删除正在下载任务
     *
     * @param position 位置
     */
    private void deleteDownLoading(String infoHash, String uri, final int position) {
        //弹出删除任务Dialog
        Dialog dialog = new DeleteDialog.Builder(this)
                .setOnclickListener(new DeleteDialog.OnclickListener() {
                    @Override
                    public void onClick(boolean checked) {
                        //把右边资源设置为可添加状态
                        //把资源设置为未添加状态
                        for (ItemBean itemBean : mainBeanList) {
                            if (itemBean.getZHNAME() != null && itemBean.getZHNAME().get(0).equals(uri)) {
                                itemBean.setAdd(false);
                                itemBean.setStop(false);
                                mainAdapter.notifyItemChanged(mainBeanList.indexOf(itemBean));
                                break;
                            }
                        }
                        MainUtils.getInstance().removeTask(infoHash, handler);
                    }

                    @Override
                    public void cancel() {

                    }
                }).create();
        if (dialog != null) {
            dialog.show();
        }
    }

    /**
     * 添加下载任务
     *
     * @param notify
     * @param position
     */
    public void addTask(boolean notify, int position) {
        ItemBean mainBean = mainBeanList.get(position);
        if (mainBean != null) {
            if (mainBean.isAdd()) {
                ToasUtil.showLongToast("您已添加该任务！");
                return;
            }
            if (downingAdapter.getItemCount() >= Constant.DOWNING_COUNT) {
                ToasUtil.showLongToast("已达到最大下载数量！");
                return;
            }
            String size = mainBean.getSIZE().get(0);
            String str2 = "";
            if (size != null && size.length() > 0) {
                for (int i = 0; i < size.length(); i++) {
                    if (size.charAt(i) >= 48 && size.charAt(i) <= 57) {
                        str2 += size.charAt(i);
                    }
                }
            }
            if (!"".equals(str2)) {
                if ((getRomAvailableSize() / 1024 / 1024) < Long.valueOf(str2)) {
                    ToasUtil.showLongToast("内存空间不足，请及时清理内存！");
                    return;
                }
            }
            //添加任务到等待下载列表
            mainBean.setAdd(true);
            if (notify) {
                mainAdapter.notifyItemChanged(position);
            }
            MainUtils.getInstance().addTask(mainBean.getURL(), handler);
        }
    }


    /**
     * 获取手机内部存储空间
     *
     * @param context
     * @return 以M, G为单位的容量
     */
    public String getInternalMemorySize(Context context) {
        return Formatter.formatFileSize(context, ROM_USAGE[1]);
    }

    /**
     * 获取手机内部已用存储空间
     *
     * @param context
     * @return 以M, G为单位的容量
     */
    public String getAvailableInternalMemorySize(Context context) {
        try {
            return Formatter.formatFileSize(context, getMemoryUse());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }


    /**
     * 获取已用的进度
     *
     * @return
     */
    public int getMemoryProgress() {
        if (ROM_USAGE[1] == 0) {
            return 0;
        } else {
            return (int) (getMemoryUse() * 100 / getRomTotalSize());
        }
    }


    /**
     * 获取已用内存
     *
     * @return
     */
    public long getMemoryUse() {
        return (ROM_USAGE[1] - ROM_USAGE[0]);
    }


    /**
     * 获得机身内存大小
     *
     * @return
     */
    public long getRomTotalSize() {
        return ROM_USAGE[1];
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    private long getRomAvailableSize() {
        return ROM_USAGE[0];

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (verifyDialog != null && verifyDialog.isShowing()) {
            verifyDialog.dismiss();
        }
        super.onDestroy();
    }

}