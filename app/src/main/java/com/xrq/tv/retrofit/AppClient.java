package com.xrq.tv.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.xrq.tv.constant.Constant;
import com.xrq.tv.utils.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/12/10.
 */
public class AppClient {
    //请求地址
    private static final String BASE_URL = Constant.URL_PATH;
    private static final String DOWN_URL = Constant.URL_DOWN_PATH;
    private static Retrofit retrofit;
    //log拦截器
    private static Interceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
            Log.iii("LOG", "OkHttp====Message:" + message)).setLevel(HttpLoggingInterceptor.Level.BODY
    );
    private static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //获取request
            Request request = chain.request();
            //获取request的创建者builder
            Request.Builder builder = request.newBuilder();
            //从request中获取headers，通过给定的键url_name
            List<String> headerValues = request.headers("url_name");
            if (headerValues != null && headerValues.size() > 0) {
                //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
                builder.removeHeader("url_name");
                //匹配获得新的BaseUrl
                String headerValue = headerValues.get(0);
                HttpUrl newBaseUrl = null;
                if ("download".equals(headerValue)) {
                    newBaseUrl = HttpUrl.parse(DOWN_URL);
                } else if ("normal".equals(headerValue)) {
                    newBaseUrl = HttpUrl.parse(BASE_URL);
                } else {
                    newBaseUrl = HttpUrl.parse(BASE_URL);
                }
                //从request中获取原有的HttpUrl实例oldHttpUrl
                HttpUrl oldHttpUrl = request.url();
                //重建新的HttpUrl，修改需要修改的url部分
                HttpUrl newFullUrl = oldHttpUrl
                        .newBuilder()
                        .scheme(newBaseUrl.scheme())
                        .host(newBaseUrl.host())
                        .port(newBaseUrl.port())
                        .build();

                //重建这个request，通过builder.url(newFullUrl).build()；
                //然后返回一个response至此结束修改
                return chain.proceed(builder.url(newFullUrl).build());
            } else {
                return chain.proceed(request);
            }
        }
    };

    public Retrofit getRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(Constant.TIME_OUT, TimeUnit.SECONDS);
        //OkHttp进行添加拦截器loggingInterceptor
        builder.addInterceptor(loggingInterceptor);
        builder.addInterceptor(interceptor);
        Interceptor requestInterceptor = chain -> {
            Request request = chain.request();
            Request compressedRequest;
            compressedRequest = request.newBuilder()
                    .header("Accept-Language", Locale.getDefault().toString())
                    .header("Accept-Charset", "UTF-8")
                    .header("Connection", "Keep-Alive")
                    .build();
            Response response = chain.proceed(compressedRequest);

            return response;
        };
        builder.interceptors().add(requestInterceptor);

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private AppClient() {
    }

    public static AppClient getInstance() {
        return AppClientHolder.APP_CLIENT;
    }

    private static class AppClientHolder {
        private static final AppClient APP_CLIENT = new AppClient();
    }
}
