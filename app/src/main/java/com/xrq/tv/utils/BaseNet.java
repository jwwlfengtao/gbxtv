package com.xrq.tv.utils;

import com.xrq.tv.retrofit.ApiStrores;
import com.xrq.tv.retrofit.AppClient;

import retrofit2.Retrofit;

/**
 * @author 765773123
 * @date 2019/1/2
 */
public class BaseNet {
    protected static final Retrofit retrofit = AppClient.getInstance().getRetrofit();
    protected static final ApiStrores apiStrores = retrofit.create(ApiStrores.class);
}
