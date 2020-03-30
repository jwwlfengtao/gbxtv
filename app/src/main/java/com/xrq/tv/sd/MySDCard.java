package com.xrq.tv.sd;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 杨铭 Created by kys_8 on 2016/1/7. <p>Email:771365380@qq.com</p> <p>Mobile phone:15133350726</p>
 */
public class MySDCard
{
    private String tag = this.getClass().getName();
    private Context context;

    /**
     构造方法
     */
    public MySDCard(Context context)
    {
        this.context = context;
    }

    //==========================================================================================
    //http://www.open-open.com/code/view/1433585940578从这里看来获取手机SDCard路径的方法有三种，
    //现有的做法是通过反射调用系统的方法获取Storage列表。——实际上有的时候这会失败，所以我们在探寻一个“全能的”方法，也或者分版本分类治之

    //    /**
    //     * 遍历 "system/etc/vold.fstab” 文件，获取全部的Android的挂载点信息
    //     *
    //     * @return
    //     */
    //    private static ArrayList<String> getDevMountList() {
    //        String[] toSearch = FileUtils.readFile("/etc/vold.fstab").split(" ");
    //        ArrayList<String> out = new ArrayList<String>();
    //        for (int i = 0; i < toSearch.length; i++) {
    //            if (toSearch[i].contains("dev_mount")) {
    //                if (new File(toSearch[i + 2]).exists()) {
    //                    out.add(toSearch[i + 2]);
    //                }
    //            }
    //        }
    //        return out;
    //    }

    //region  getSDCardPathsVersionID()
    public File[] getSDCardPaths()
    {
        Log.e(tag, "当前手机系统版本号=" + Build.VERSION.SDK_INT);
        switch (Build.VERSION.SDK_INT)
        {
            case 23://23
                return this.getSDCardPaths_23();
            case 22://22
                return this.getSDCardPaths_22();
            case 21:
                return this.getSDCardPaths_21();
            case 20:
                return this.getSDCardPaths_20();
            case 19:
                return this.getSDCardPaths_19();
            case 18:
                return this.getSDCardPaths_18();
            case 17:
                return this.getSDCardPaths_17();
            case 16:
                return this.getSDCardPaths_16();
            case 15:
                return this.getSDCardPaths_15();
            case 14:
                return this.getSDCardPaths_14();
            //            case 13:
            //                return this.getSDCardPaths_13();
            //            case 12:
            //                return this.getSDCardPaths_12();
            //            case 11:
            //                return this.getSDCardPaths_11();
            //            case 10:
            //                return this.getSDCardPaths_10();
            //            case 9:
            //                return this.getSDCardPaths_9();
            //            case 8:
            //                return this.getSDCardPaths_8();
        }
        Log.e(tag, "不支持这个版本，请查阅源码");
        return null;
    }

    /**
     通过使用Environment.getExternalStorageDirectory()所使用的方法得到本机中所有存储卡的位置
     <p/>
     这个总体思路是对的，但是被一些具体的实现给卡住了
     */
    @TargetApi(23)//这里反射的方法之后api23才有所以我们这个方法是针对这个版本的
    public File[] getSDCardPaths_23()
    {
        _Log.e(_Log.msg() + "我们得到的mUID=" + Environment.getExternalStorageDirectory().getPath());
        try
        {//我们的思路：首先得到方法-然后执行方法
            Class<?> class_UserEnvironment = Class
                    .forName("android.os.Environment$UserEnvironment");
            Method method_getExternalDirs = class_UserEnvironment.getMethod("getExternalDirs");
            //方法的执行需要这个方法所在类的一个对象
            Constructor<?> ueConstructor = class_UserEnvironment
                    .getConstructor(new Class[]{int.class});
            //这里可以自己初始化，可以以直接通过Environment来调用
            Method method_myUserId = UserHandle.class.getMethod("myUserId");
            int mUId = (int) method_myUserId
                    .invoke(null);//如果底层方法所需的形参数为 0，则所提供的 args 数组长度可以为 0 或 null。
            _Log.e(_Log.msg() + "我们得到的mUID=" + mUId);
            //ueConstructor.newInstance(mUId);这个方法就是获取到类对象的方法
            File[] files = (File[]) method_getExternalDirs
                    .invoke(ueConstructor.newInstance(mUId));//以上的一切都是为了这一句服务的
            _Log.e(_Log.msg() + "File[].length=" + files.length);
            for (File value : files)
            {
                _Log.d(_Log.msg() + "-->" + value.getPath());
            }
            return files;
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     在真机对应版本上进行测试没有任何问题， 但是我还有一个问题，就是为什么在模拟器上就得不到这样一个结果，是因为模拟器的源码和真机的有不同，还是我创建真机的时候有异常？

     @return 获取到的存储设备
     */
    @TargetApi(22)
    public File[] getSDCardPaths_22()
    {
        _Log.e(_Log.msg() + "getExternalStorageDirectory=" + Environment
                .getExternalStorageDirectory().getPath());
        try
        {//我们的思路：初始化内部类，调用方法
            Class<?> class_UserEnvironment = Class
                    .forName("android.os.Environment$UserEnvironment");
            Method method_getExternalDirsForApp = class_UserEnvironment
                    .getMethod("getExternalDirsForApp");
            //方法的执行需要这个方法所在类的一个对象
            Constructor<?> ueConstructor = class_UserEnvironment
                    .getConstructor(new Class[]{int.class});
            //这里可以自己初始化，可以以直接通过Environment来调用
            Method method_myUserId = UserHandle.class.getMethod("myUserId");
            int mUId = (int) method_myUserId.invoke(null);
            _Log.e(_Log.msg() + "我们得到的mUID=" + mUId);
            //以上的一切都是为了下一句一句服务的
            File[] files = (File[]) method_getExternalDirsForApp
                    .invoke(ueConstructor.newInstance(mUId));

            _Log.e(_Log.msg() + "File[].length=" + files.length);
            for (File value : files)
            {
                //boolean isRemovable = (boolean) method_isRemovable.invoke(value);//是否可以移除
                //String getVolumeState = (String) method_getVolumeState.invoke(sm, path);//获取挂载状态。
                _Log.d(_Log.msg() + "-->" + value.getPath());
            }
            return files;
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。

     @return 获取到的存储设备
     */
    @TargetApi(21)
    public File[] getSDCardPaths_21()
    {
        return getSDCardPaths_22();
    }

    /**
     如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。

     @return 获取到的存储设备
     */
    @TargetApi(20)
    public File[] getSDCardPaths_20()
    {
        return getSDCardPaths_22();
    }

    /**
     如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。

     @return 获取到的存储设备
     */
    @TargetApi(19)
    public File[] getSDCardPaths_19()
    {
        return getSDCardPaths_22();
    }

    /**
     如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
     <p/>
     借鉴：http://vjson .com/wordpress/%E8%8E%B7%E5%8F%96android%E8%AE%BE%E5%A4%87%E6%8C%82%E8%BD%BD
     %E7%9A%84%E6%89%80%E6%9C%89%E5%AD%98%E5%82%A8%E5%99%A8.html

     @return 获取到的存储设备
     */
    @TargetApi(18)
    public File[] getSDCardPaths_18()
    {
        try
        {
            _Log.e(_Log.msg() + "getExternalStorageDirectory=" + Environment
                    .getExternalStorageDirectory().getPath());
            Class class_StorageManager = StorageManager.class;
            Method method_getVolumeList = class_StorageManager.getMethod("getVolumeList");
            Method method_getVolumeState = class_StorageManager
                    .getMethod("getVolumeState", String.class);
            StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Class class_StorageVolume = Class.forName("android.os.storage.StorageVolume");
            Method method_isRemovable = class_StorageVolume.getMethod("isRemovable");
            Method method_getPath = class_StorageVolume.getMethod("getPath");
            Method method_getPathFile = class_StorageVolume.getMethod("getPathFile");
            Object[] objArray = (Object[]) method_getVolumeList.invoke(sm);

            _Log.e(_Log.msg() + "objArray[].length=" + objArray.length + "---根据是否可以移除来判断是否为外置存储卡。");
            List<File> fileList = new ArrayList<>();
            for (Object value : objArray)
            {
                String path = (String) method_getPath.invoke(value);
                boolean isRemovable = (boolean) method_isRemovable.invoke(value);
                String getVolumeState = (String) method_getVolumeState.invoke(sm, path);//获取挂载状态。
                _Log.d(_Log.msg() + "存储路径：" + path + "---isRemovable:" + isRemovable +
                               "----getVolumeState:" + getVolumeState);

                fileList.add((File) method_getPathFile.invoke(value));
            }
            File[] files = new File[fileList.size()];
            fileList.toArray(files);
            return files;
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。

     @return 获取到的存储设备
     */
    @TargetApi(17)
    public File[] getSDCardPaths_17()
    {
        return getSDCardPaths_18();
    }

    /**
     如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。
     <p/>
     api16在StorageVolume方法中没有getPathFile

     @return 获取到的存储设备
     */
    @TargetApi(16)
    public File[] getSDCardPaths_16()
    {
        try
        {
            _Log.e(_Log.msg() + "getExternalStorageDirectory=" + Environment
                    .getExternalStorageDirectory().getPath());
            Class class_StorageManager = StorageManager.class;
            Method method_getVolumeList = class_StorageManager.getMethod("getVolumeList");
            Method method_getVolumeState = class_StorageManager
                    .getMethod("getVolumeState", String.class);
            StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Class class_StorageVolume = Class.forName("android.os.storage.StorageVolume");
            Method method_isRemovable = class_StorageVolume.getMethod("isRemovable");
            Method method_getPath = class_StorageVolume.getMethod("getPath");
            Object[] objArray = (Object[]) method_getVolumeList.invoke(sm);

            _Log.e(_Log.msg() + "objArray[].length=" + objArray.length + "---根据是否可以移除来判断是否为外置存储卡。");
            List<File> fileList = new ArrayList<>();
            for (Object value : objArray)
            {
                String path = (String) method_getPath.invoke(value);
                boolean isRemovable = (boolean) method_isRemovable.invoke(value);
                String getVolumeState = (String) method_getVolumeState.invoke(sm, path);//获取挂载状态。
                _Log.d(_Log.msg() + "存储路径：" + path + "---isRemovable:" + isRemovable +
                               "----getVolumeState:" + getVolumeState);

                fileList.add(new File((String) method_getPath.invoke(value)));
            }

            File[] files = new File[fileList.size()];
            fileList.toArray(files);
            return files;
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。

     @return 获取到的存储设备
     */
    @TargetApi(15)
    public File[] getSDCardPaths_15()
    {
        return getSDCardPaths_16();
    }

    /**
     如果调用高版本的实现方式，说明这两个版本的我们用到的源码是相同的。

     @return 获取到的存储设备
     */
    @TargetApi(14)
    public File[] getSDCardPaths_14()
    {
        return getSDCardPaths_16();
    }
    //endregion
}
