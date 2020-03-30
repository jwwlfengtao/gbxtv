package com.xrq.tv.utils;

import android.os.Handler;
import android.os.Message;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @author 765773123
 * @date 2018/12/1
 * 检查是否为VIP，VIP是否过期，申请VIP
 */
public class CheckVipUtils extends BaseNet{
    private boolean isVip = true;

    private CheckVipUtils() {

    }

    public static CheckVipUtils getInstance() {
        return CheckVipUtilsHoder.CHECK_VIP_UTILS;
    }

    private static class CheckVipUtilsHoder {
        private static final CheckVipUtils CHECK_VIP_UTILS = new CheckVipUtils();
    }

    public boolean isVip() {
        if (DataModel.getInstance().getToken() == null || "".equals(DataModel.getInstance().getToken())) {
            return false;
        } else {
            return isVip;
        }
    }

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
    /**
     * 注册Vip
     * @param code 验证码
     * @param handler 更新注册情况
     */
    public void registerVip(String code, Handler handler) {
        if (code == null || "".equals(code)
                || code.length() != 8) {
            if (handler != null) {
                Message message = new Message();
                message.what = 0x123;
                message.obj = "请输入正确的激活码";
                handler.sendMessage(message);
            }
            return;
        }

        apiStrores.registerVip(code, getNewMac())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Log.iii("TAG", "开通VIP：" + jsonObject.toString());
                        try {
                            JSONObject jsonObject1 = new JSONObject(jsonObject.toString());
                            switch (jsonObject1.getInt("err_code")) {
                                case 0:
                                    Log.iii("TAG", "开通VIP成功：" + jsonObject.toString());
                                    String token = jsonObject1.getString("token");
                                    String expire = jsonObject1.getString("expire");
                                    DataModel.getInstance().saveToken(token);
                                    DataModel.getInstance().saveExpireTime(expire);
                                    if (handler != null) {
                                        Message message = new Message();
                                        message.what = 0x1234;
                                        message.obj = "恭喜你，你已成功激活!";
                                        handler.sendMessage(message);
                                    }
                                    break;
                                case -1:
                                default:
                                    Log.iii("TAG", "开通VIP失败：" + jsonObject.toString());
                                    String mes = jsonObject1.getString("err_desc");
                                    if (handler != null) {
                                        Message message = new Message();
                                        message.what = 0x123;
                                        message.obj = mes;
                                        handler.sendMessage(message);
                                    }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.iii("TAG", "开通失败：" + e.toString());
                            if (handler != null) {
                                Message message = new Message();
                                message.what = 0x123;
                                message.obj = e.toString();
                                handler.sendMessage(message);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.iii("TAG", "开通失败：" + t.toString());
                        if (handler != null) {
                            Message message = new Message();
                            message.what = 0x123;
                            message.obj = "激活失败" + t.toString();
                            handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
