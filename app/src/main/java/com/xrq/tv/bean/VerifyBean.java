package com.xrq.tv.bean;

import java.io.Serializable;

public class VerifyBean implements Serializable {
    //    "err_code":-1,"err_desc":"激活码不存在!","token"
    private int err_code;
    private String err_desc;
    private String token;

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    public String getErr_desc() {
        return err_desc;
    }

    public void setErr_desc(String err_desc) {
        this.err_desc = err_desc;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
