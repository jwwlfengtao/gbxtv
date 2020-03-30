package com.xrq.tv.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.xrq.tv.application.App;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by Administrator on 2017/3/29.
 */
public class SharedPreferencesUtils {
    public static final String FIRST_USE_APP = "isFirstUseApp";


    private SharedPreferencesUtils() {
    }

    /**
     * 获取SharedPreferences
     *
     * @return SharedPreferences
     */
    public static SharedPreferences getSharedPreferences() {
        return SharedPreferencesHolder.sp;
    }

    private static class SharedPreferencesHolder {
        private static final SharedPreferences sp = App.getApp().getSharedPreferences("TV", Context.MODE_PRIVATE);
    }


    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }


    /**
     * 清除所有保存的数据
     */
    public static void cleanAll() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        editor.putBoolean(FIRST_USE_APP, false);
        editor.commit();
    }

    /**
     * 存入对象
     */
    public static <T> void save(String key, T value) {
        try {
            SharedPreferences sp = getSharedPreferences();
            SharedPreferences.Editor e = sp.edit();

            //把driver对象转换为字符串存入
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);

            //将对象序列化写入byte缓存
            os.writeObject(value);

            //将序列化的数据转为16进制保存
            String bytesToHexString = bytesToHexString(bos.toByteArray());

            bos.flush();
            os.flush();


            //保存该16进制数组
            e.putString(key, bytesToHexString);
            e.commit();
            bos.close();
            os.close();
        } catch (Exception e) {
        }
    }

    /**
     * 获取保存的对象
     */
    public static <T> T get(String key) {
        try {
            SharedPreferences sp = getSharedPreferences();
            if (sp.contains(key)) {
                String string = sp.getString(key, "");
                if (TextUtils.isEmpty(string)) {
                    return null;
                } else {
                    //将16进制的数据转为数组，准备反序列化
                    byte[] stringToBytes = StringToBytes(string);
                    if (stringToBytes == null || stringToBytes.length == 0) {
                        return null;
                    }
                    ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                    ObjectInputStream is = new ObjectInputStream(bis);

                    //返回反序列化得到的对象
                    return (T) is.readObject();
                }
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //所有异常返回null
        return null;
    }

    /**
     * 清除保存的对象
     */
    public static void clear(String key) {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor e = sp.edit();
        if (sp.contains(key)) {
            e.remove(key);
        }
        e.commit();

    }

    /**
     * desc:将16进制的数据转为数组
     */
    private static byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
            int int_ch1;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch1 = (hex_char1 - 48) * 16;   //// 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch1 = (hex_char1 - 55) * 16; //// A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
            int int_ch2;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch2 = (hex_char2 - 48); //// 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch2 = hex_char2 - 55; //// A 的Ascll - 65
            else
                return null;
            int_ch = int_ch1 + int_ch2;
            retData[i / 2] = (byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }

    /**
     * desc:将数组转为16进制
     */
    private static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
}
