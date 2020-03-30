package com.xrq.tv.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


//升级程序
public class UpgradeUtil {
    private static final String TAG = "upgrade";
    private Context mContext;
    private String mBaseUrl;
    private Handler mHandler;
    private boolean needUpgrade = false;
    private Uri newApkUri;

    private boolean download(String url, File saveto) {
        HttpURLConnection urlConnection = null;
        try {
            //创建其对象
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            //设置连接时间，10秒
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setReadTimeout(10 * 1000);
            //数据编码格式，这里utf-8
            urlConnection.setRequestProperty("Charset", "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            //开启客户端与Url所指向的资源的网络连接
            urlConnection.connect();

            if (200 == urlConnection.getResponseCode()) {//HTTP_OK 即200，连接成功的状态码
                FileOutputStream fout = new FileOutputStream(saveto);
                    byte[] b = new byte[32*1024];
                    int length;
                    while ((length = urlConnection.getInputStream().read(b)) > 0) {
                        fout.write(b, 0, length);
                    }
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (urlConnection != null) {
                    //解除连接，释放网络资源
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }
    private String getContent(String url) {
        HttpURLConnection urlConnection = null;
        try {
            //创建其对象
            urlConnection = (HttpURLConnection) new URL(this.mBaseUrl + "/version.txt").openConnection();
            //设置连接时间，10秒
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setReadTimeout(10 * 1000);
            //数据编码格式，这里utf-8
            urlConnection.setRequestProperty("Charset", "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        String result = null;

        try {
            //开启客户端与Url所指向的资源的网络连接
            urlConnection.connect();

            if (200 == urlConnection.getResponseCode()) {//HTTP_OK 即200，连接成功的状态码
                if (urlConnection.getContentLength() > 0) {
                    bufferedInputStream = new
                            BufferedInputStream(urlConnection.getInputStream());
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    //httpUrlConnection返回传输字节的长度，创建一个byte 数组。
                    byte[] b = new byte[urlConnection.getContentLength()];
                    int length;
                    while ((length = bufferedInputStream.read(b)) > 0) {
                        byteArrayOutputStream.write(b, 0, length);
                    }
                    result = byteArrayOutputStream.toString("utf-8");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if(bufferedInputStream!=null){
                    bufferedInputStream.close();
                }
                if (urlConnection != null) {
                    //解除连接，释放网络资源
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;

    }

    Runnable checkThread = new Runnable() {
        @Override
        public void run() {
            String content = getContent(UpgradeUtil.this.mBaseUrl + "/version.txt");
            if(content == null) return ;
            String[] ary = content.split(",");
            int vcode = Integer.parseInt(ary[0]);
            Log.d(TAG, "new version" + vcode);
            PackageInfo pInfo = null;
            try {
                pInfo = UpgradeUtil.this.mContext.getPackageManager().getPackageInfo(UpgradeUtil.this.mContext.getPackageName(), 0);
                if(pInfo.versionCode >= vcode) return;

                UpgradeUtil.this.mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                final File[] file = UpgradeUtil.this.mContext.getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS);

                String saveto = file[0].getAbsolutePath() + "/" + ary[1];
                File fsaveto = new File(saveto);
                Log.d(TAG, "prepare download"  + fsaveto.getAbsolutePath() );
                boolean r = download(UpgradeUtil.this.mBaseUrl + "/" + ary[1], fsaveto);
                Log.d(TAG, "download result "  + r);

                if(r) {
                    needUpgrade = true;
                    newApkUri = Uri.fromFile(fsaveto);
                    Message message = new Message();
                    message.what = 0x8888;
                    mHandler.sendMessage(message);
                }


            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    };

    public void alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpgradeUtil.this.mContext);
        builder.setTitle("提示：");
        builder.setMessage("检测到新版本，需要升级吗?");
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(true);
        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Uri downloadFileUri = newApkUri;
                Log.d("DownloadManager", downloadFileUri.toString());
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                UpgradeUtil.this.mContext.startActivity(install);
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public UpgradeUtil(String baseUrl, Context context, Handler handler){
        this.mContext = context;;
        this.mBaseUrl = baseUrl;
        this.mHandler = handler;
    }

    public void startCheck(){
        new Thread(checkThread).start();
    }
}
