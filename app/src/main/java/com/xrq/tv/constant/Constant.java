package com.xrq.tv.constant;

/**
 * @author 765773123
 * @date 2019/1/2
 * APP一些常量配置
 */
public class Constant {
    /**
     * 左边正在下载列表个数限制
     */
    public static final int DOWNING_COUNT = 5;

    /**
     * 默认存储声音文件的文件夹名
     */
    public static final String FILE_NAME = "BD_Download";

    /**
     * 接口请求地址
     */
//    public static final String URL_PATH = "http://mq.kdsw.tk:180";
//    public static final String URL_PATH = "http://iamluodong.vicp.net:3100";
    public static final String URL_PATH = "http://cds.gostcrab.cc:180";
    public static final String URL_DOWN_PATH = "http://localhost:3100";
//    public static final String URL_DOWN_PATH = "https://n1.tr.nbmesh.com/announce";
//    public static final String URL_DOWN_PATH = "http://iamluodong.vicp.net:3100";

    /**
     * 是否是Debug模式，debug模式下将打印日志信息
     */

    /**
     * 引导页停留时间
     */
    public static final long LAUNCH_TIME = 1000 * 2;

    /**
     * 网络请求超时时间
     */
    public static final long TIME_OUT = 1000 * 20;
}
