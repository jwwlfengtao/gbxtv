package com.xrq.tv.utils;

import android.content.Context;
import android.os.StatFs;
import android.os.storage.StorageManager;

import com.xrq.tv.BuildConfig;
import com.xrq.tv.application.App;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.xrq.tv.utils.UsbUtils.State.HAVEUSB;
import static com.xrq.tv.utils.UsbUtils.State.NOPERMISSION;
import static com.xrq.tv.utils.UsbUtils.State.NOUSB;


/**
 * @author 765773123
 * @date 2018/12/10
 */
public class UsbUtils {
    private static String path1="/mnt/media_rw/sda1";
    private static String path2="/storage/sda4";

    //用于测试是否能创建文件夹
    private static final String testPath = "/test";

    public enum State {
        NOUSB, NOPERMISSION, HAVEUSB
    }

    //检测Usb是否连接
    public static State checkUsb() {
        State havePath = NOUSB;
        //动态获取路径

        if(BuildConfig.BUILD_TYPE.equals("debug") ) {
            String path = getExtendedMemoryPath();
            final String tp = "/Android/data/com.xrq.tv";
            final String npath = path + tp;
            DataModel.getInstance().saveBasePath(npath);
            return HAVEUSB;
        }
        //写死路径
        File file1 = new File(path1+ testPath);
        if (!file1.exists()){
            boolean b1=file1.mkdirs();
            if (b1){
                DataModel.getInstance().saveBasePath(path1);
                file1.delete();
                return HAVEUSB;
            }
        }else {
            boolean b1=file1.delete();
            if (b1){
                DataModel.getInstance().saveBasePath(path1);
                return HAVEUSB;
            }
        }
        //写死路径
        File file2 = new File(path2+ testPath);
        if (!file2.exists()){
            boolean b1=file2.mkdirs();
            if (b1){
                DataModel.getInstance().saveBasePath(path2);
                file2.delete();
                return HAVEUSB;
            }
        }else {
            boolean b1=file2.delete();
            if (b1){
                DataModel.getInstance().saveBasePath(path2);
                return HAVEUSB;
            }
        }
        //动态获取路径
        String path = getExtendedMemoryPath();
        if (path == null) {
            Log.iii("TAG", "路径为空");
            return NOUSB;
        } else {
            Log.iii("TAG", "获取的路径" + path);
        }
        //cmd获取读写权限
        String cmd = "777";
        execShell(cmd);
        File file = new File(path + testPath);
        Log.iii("TAG", "测试文件路径" + path + testPath);
        if (!file.exists()) {
            Log.iii("TAG", "测试文件不存在");
            boolean b = file.mkdirs();
            if (b) {
                Log.iii("TAG", "测试文件创建成功");
                boolean b1 = file.delete();
                if (b1) {
                    Log.iii("TAG", "测试文件删除成功");
                    //可读可写可删，为有效目录
                    DataModel.getInstance().saveBasePath(path);
                    havePath = HAVEUSB;
                } else {
                    Log.iii("TAG", "测试文件删除失败");
                    havePath = NOPERMISSION;
                }
            } else {
                Log.iii("TAG", "测试文件创建失败");
                //没有权限，申请权限
                havePath = NOPERMISSION;
            }
        } else {
            Log.iii("TAG", "测试文件存在");
            boolean d = file.delete();
            if (d) {
                Log.iii("TAG", "测试文件存删除成功");
                //可读可写可删，为有效目录
                DataModel.getInstance().saveBasePath(path);
                havePath = HAVEUSB;
            } else {
                Log.iii("TAG", "测试文件存删除失败");
                havePath = NOPERMISSION;
            }
        }

        return havePath;
    }

    private static void execShell(String cmd) {
        try {
            //权限设置
            Process p = Runtime.getRuntime().exec("su");
            //获取输出流
            OutputStream outputStream = p.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            //将命令写入
            dataOutputStream.writeBytes(cmd);
            //提交命令
            dataOutputStream.flush();
            //关闭流操作
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static String getExtendedMemoryPath() {
        List<String> stringList = new ArrayList<>();
        StorageManager mStorageManager = (StorageManager) App.getApp().getSystemService(Context.STORAGE_SERVICE);
        Class storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method method_getVolumeState = mStorageManager.getClass().getMethod("getVolumeState", String.class);
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Method getUserLabel = storageVolumeClazz.getMethod("getUserLabel");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                Log.iii("TAG", "path11111" + path);
                String userLabel = (String) getUserLabel.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (removable) {
                    String getVolumeState = (String) method_getVolumeState.invoke(mStorageManager, path);//获取挂载状态。
                    Log.iii("TAG", "path--" + path);
                    Log.iii("TAG", "getVolumeState--" + getVolumeState);
                    Log.iii("TAG", "userLabel---" + userLabel);
                    stringList.add(path);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (stringList.size() == 1) {
            Log.iii("TAG", "有一个可移动盘，路径为" + stringList.get(0));
            return stringList.get(0);
        } else if (stringList.size() > 1) {
            //如果有多个盘，返回内存较大的那个
            int maxIndex = 0;
            long TotalSize = 0;
            for (int i = 0; i < stringList.size(); i++) {
                Log.iii("TAG", "可移动盘" + i + "    " + stringList.get(i));
                if (getRomTotalSize(stringList.get(i)) > TotalSize) {
                    maxIndex = i;
                    TotalSize = getRomTotalSize(stringList.get(i));

                }
            }
            Log.iii("TAG", "有多个可移动盘，返回路径为" + stringList.get(maxIndex));
            return stringList.get(maxIndex);
        } else {
            Log.iii("TAG", "没有可移动盘");
            return null;
        }

    }

    /**
     * 获得机身内存大小
     *
     * @return
     */
    public static long getRomTotalSize(String path) {
        if(true) return 1024*1024*1024*1024;
        try {
            StatFs statFs = new StatFs(path);
            long blockSize = statFs.getBlockSizeLong();
            long tatalBlocks = statFs.getBlockCountLong();
            return blockSize * tatalBlocks;
        } catch (Exception e) {
            return 0;
        }

    }

}
