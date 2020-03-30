package com.xrq.tv.utils;


/**
 * 与sharedPrenfrence有关的数据
 */
public class DataModel {
    private DataModel() {
    }

    public static DataModel getInstance() {
        return DataModelHolder.DATA_MODEL;
    }

    private static class DataModelHolder {
        private static final DataModel DATA_MODEL = new DataModel();
    }

    public String getToken() {
        return SharedPreferencesUtils.getString("Token");
    }

    public void saveToken(String token) {
        SharedPreferencesUtils.putString("Token", token);
    }

    public String getStorePath() {
        return SharedPreferencesUtils.getString("Path");
    }

    public void saveStorePath(String path) {
        SharedPreferencesUtils.putString("Path", path);
    }

    public String getExpireTime() {
        return SharedPreferencesUtils.getString("Expire");
    }

    public void saveExpireTime(String path) {
        SharedPreferencesUtils.putString("Expire", path);
    }

    public void saveBasePath(String path) {
        SharedPreferencesUtils.putString("BasePath", path);
    }

    public String getBasePath() {

        return SharedPreferencesUtils.getString("BasePath");
    }
}
