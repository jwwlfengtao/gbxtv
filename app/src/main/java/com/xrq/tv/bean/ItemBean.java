package com.xrq.tv.bean;

import android.view.View;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

public class ItemBean implements Serializable {
    private List<String> ZHNAME;
    private List<String> ENNAME;
    private List<String> YEAR;
    private List<String> SCORE;
    private List<String> ABSTRACT;
    private List<String> SIZE;
    private List<String> POSTER;
    private List<TagsBean> TAGS;
    private String URL;
    private String DATA;
    private boolean isSelected;
    private boolean isNull = false;
    private int ivisibleTime = View.GONE;
    private boolean isAdd;
    private boolean haveFoces;
    //文件状态 1，下载完成，0未下载完成
    private int state;
    //保存到硬盘的文件名
    private String saveFileName;
    private boolean isDelete;//该任务是否被删除
    private String STATUS;
    //下载进度
    private int progerss;
    //是否暂停下载
    private boolean isStop;
    //状态名
    private String stateName = "等待下载";
    private String infoHash;

    public String getInfoHash() {
        return infoHash;
    }

    public void setInfoHash(String infoHash) {
        this.infoHash = infoHash;
    }

    public int getProgerss() {
        return progerss;
    }

    public void setProgerss(int progerss) {
        this.progerss = progerss;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }



    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isHaveFoces() {
        return haveFoces;
    }

    public void setHaveFoces(boolean haveFoces) {
        this.haveFoces = haveFoces;
    }

    public List<String> getZHNAME() {
        return ZHNAME;
    }

    public void setZHNAME(List<String> ZHNAME) {
        this.ZHNAME = ZHNAME;
    }

    public List<String> getENNAME() {
        return ENNAME;
    }

    public void setENNAME(List<String> ENNAME) {
        this.ENNAME = ENNAME;
    }

    public List<String> getYEAR() {
        return YEAR;
    }

    public void setYEAR(List<String> YEAR) {
        this.YEAR = YEAR;
    }

    public List<String> getSCORE() {
        return SCORE;
    }

    public void setSCORE(List<String> SCORE) {
        this.SCORE = SCORE;
    }

    public List<String> getABSTRACT() {
        return ABSTRACT;
    }

    public void setABSTRACT(List<String> ABSTRACT) {
        this.ABSTRACT = ABSTRACT;
    }

    public List<String> getSIZE() {
        return SIZE;
    }

    public void setSIZE(List<String> SIZE) {
        this.SIZE = SIZE;
    }

    public List<String> getPOSTER() {
        return POSTER;
    }

    public void setPOSTER(List<String> POSTER) {
        this.POSTER = POSTER;
    }

    public List<TagsBean> getTAGS() {
        return TAGS;
    }

    public void setTAGS(List<TagsBean> TAGS) {
        this.TAGS = TAGS;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    public int getIvisibleTime() {
        return ivisibleTime;
    }

    public void setIvisibleTime(int ivisibleTime) {
        this.ivisibleTime = ivisibleTime;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
