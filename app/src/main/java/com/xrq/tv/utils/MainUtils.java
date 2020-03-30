package com.xrq.tv.utils;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xrq.tv.bean.AddTaskBean;
import com.xrq.tv.bean.AllTaskBean;
import com.xrq.tv.bean.DateBean;
import com.xrq.tv.bean.RootBean;

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
 * @date 2018/12/13
 * 首页，获取音频列表
 */
public class MainUtils extends BaseNet {

    private MainUtils() {

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

    public static MainUtils getInstance() {
        return MainUtilsHolder.MAIN_UTILS;
    }

    private static class MainUtilsHolder {
        private static final MainUtils MAIN_UTILS = new MainUtils();
    }

    public void getListData(Handler handler) {
        String token = DataModel.getInstance().getToken();
        if (token == null || "".equals(token)) {
            if (handler != null) {
                Message message = new Message();
                message.what = 0x123;
                message.obj = "Token失效";
                handler.sendMessage(message);
            }
            return;
        }
        apiStrores.getList(token, "json", getNewMac())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(JsonObject xmlService) {
                        Log.iii("TAG", "获取成功jsonObject" + xmlService.toString());
                        if (handler == null) {
                            Log.iii("TAG", "handler is null");
                            return;
                        }
                        try {
                            JSONObject object = new JSONObject(xmlService.toString());
                            RootBean rootBean = new Gson().fromJson(object.getString("ROOT"), new TypeToken<RootBean>() {
                            }.getType());
                            if (rootBean != null) {
                                List<DateBean> dateBeanLis = rootBean.getDATE();
                                if (dateBeanLis != null) {
                                    Message message = new Message();
                                    message.what = 0x1234;
                                    message.obj = dateBeanLis;
                                    handler.sendMessage(message);
                                } else {
                                    Message message = new Message();
                                    message.what = 0x12345;
                                    message.obj = "空数据";
                                    handler.sendMessage(message);
                                }
                            } else {
                                Message message = new Message();
                                message.what = 0x12345;
                                message.obj = "空数据";
                                handler.sendMessage(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Message message = new Message();
                            message.what = 0x123;
                            message.obj = "Token失效";
                            handler.sendMessage(message);
                        }


                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.iii("TAG", "其他错误" + t.toString());
                        if (t.toString().contains("401")) {
                            if (handler != null) {
                                Message message = new Message();
                                message.what = 0x123;
                                message.obj = "Token失效";
                                handler.sendMessage(message);
                            }
                        } else {
                            if (handler != null) {
                                Message message = new Message();
                                message.what = 0x123;
                                message.obj = "其他错误";
                                handler.sendMessage(message);
                            }
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void addTask(String magnet, Handler handler) {
        String token = DataModel.getInstance().getToken();
        if (token == null || "".equals(token)) {
            if (handler != null) {
                Message message = new Message();
                message.what = 0x123;
                message.obj = "Token失效";
                handler.sendMessage(message);
            }
            return;
        }
        apiStrores.addTask(token, magnet)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddTaskBean>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(AddTaskBean xmlService) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getTaskList(Handler handler) {
        String token = DataModel.getInstance().getToken();
        if (token == null || "".equals(token)) {
            if (handler != null) {
                Message message = new Message();
                message.what = 0x123;
                message.obj = "Token失效";
                handler.sendMessage(message);
            }
            return;
        }
        apiStrores.getTaskList(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AllTaskBean>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(AllTaskBean data) {
                        Log.iii("TAG","左侧数据111111111");
                        Message message = new Message();
                        message.what = 0x123456;
                        message.obj = data;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.iii("TAG","左侧数据报错了"+t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void startTask(String infoHash, Handler handler) {
        String token = DataModel.getInstance().getToken();
        if (token == null || "".equals(token)) {
            if (handler != null) {
                Message message = new Message();
                message.what = 0x123;
                message.obj = "Token失效";
                handler.sendMessage(message);
            }
            return;
        }
        apiStrores.startTask(token, infoHash)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(JsonObject xmlService) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void stopTask(String infoHash, Handler handler) {
        String token = DataModel.getInstance().getToken();
        if (token == null || "".equals(token)) {
            if (handler != null) {
                Message message = new Message();
                message.what = 0x123;
                message.obj = "Token失效";
                handler.sendMessage(message);
            }
            return;
        }
        apiStrores.stopTask(token, infoHash)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(JsonObject xmlService) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void removeTask(String infoHash, Handler handler) {
        String token = DataModel.getInstance().getToken();
        if (token == null || "".equals(token)) {
            if (handler != null) {
                Message message = new Message();
                message.what = 0x123;
                message.obj = "Token失效";
                handler.sendMessage(message);
            }
            return;
        }
        apiStrores.removeTask(token, infoHash,0)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(JsonObject xmlService) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
