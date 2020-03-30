package com.xrq.tv.retrofit;

import com.google.gson.JsonObject;
import com.xrq.tv.bean.AddTaskBean;
import com.xrq.tv.bean.AllTaskBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 地址
 *
 * @author 765773123
 * @date 2018/11/5
 */
public interface ApiStrores {

    /**
     * 注册VIP
     *
     * @param code 验证码
     * @return JsonObject
     */
    @Headers("url_name:normal")
    @POST("api/users/{code}/active")
    Flowable<JsonObject> registerVip(@Path("code") String code, @Query("mcode") String mcode);

    /**
     * 获取列表数据
     *
     * @param token  用户登录获取的Token值
     * @param format json还是xml格式
     * @return JsonObject
     */
    @Headers("url_name:normal")
    @GET("api/media")
    Flowable<JsonObject> getList(@Query("token") String token, @Query("format") String format, @Query("mcode") String mcode);

    /**
     * 增加magnet任务
     *
     * @param token
     * @param magnet
     * @return
     */
    @Headers("url_name:download")
    @POST("api/add")
    Flowable<AddTaskBean> addTask(@Query("token") String token, @Query("magnet") String magnet);

    /**
     * 列车所有列表任务
     *
     * @param token
     * @return
     */
    @Headers("url_name:download")
    @POST("api/list")
    Flowable<AllTaskBean> getTaskList(@Query("token") String token);

    /**
     * 启动任务
     * +
     *
     * @param token
     * @param infoHash
     * @return
     */
    @Headers("url_name:download")
    @POST("api/start")
    Flowable<JsonObject> startTask(@Query("token") String token, @Query("infoHash") String infoHash);

    /**
     * 停止任务
     *
     * @param token
     * @param infoHash
     * @return
     */
    @Headers("url_name:download")
    @POST("api/stop")
    Flowable<JsonObject> stopTask(@Query("token") String token, @Query("infoHash") String infoHash);

    /**
     * 删除任务
     *
     * @param token
     * @param infoHash
     * @return
     */
    @Headers("url_name:download")
    @POST("api/remove")
    Flowable<JsonObject> removeTask(@Query("token") String token, @Query("infoHash") String infoHash,@Query("isDelete")int isDelete);
}
