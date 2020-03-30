package com.xrq.tv.bean;

import java.io.Serializable;
import java.util.List;

public class TagsBean implements Serializable{
    private List<String> TAG;

    public List<String> getTAG() {
        return TAG;
    }

    public void setTAG(List<String> TAG) {
        this.TAG = TAG;
    }
}
