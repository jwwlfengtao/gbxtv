package com.xrq.tv.bean;

import java.util.List;

public class DateBean {
    /**
     * $ : {"VALUE":"2018-10-08","STATUS":"1"}
     * item : [{"zhname":["鱼王"],"enname":["yuwang"],"year":["2017"],"score":["9.0"],"abstract":["长篇小说《鱼王》是阿斯塔菲耶夫最具个性的一部代表作"],"size":["10MB"],"poster":["http://111.161.65.188:180/resources/yuwang.jpg"],"tags":[{"tag":["中文","英文","多语言"]}],"url":["magnet:?xt=urn:btih:2451a5157f5dd466b2eb9280ae3a48fb51eceb4c"]},{"zhname":["时间中的孩子"],"enname":["The Child in Time"],"year":["2018"],"score":["5.9"],"abstract":["成长是一种失去，失去童年的特权"],"size":["10MB"],"poster":["http://111.161.65.188:180/resources/s29890774.jpg"],"tags":[{"tag":["中文","英文"]}],"url":["magnet:?xt=urn:btih:2451a5157f5dd466b2eb9280ae3a48fb51eceb4c"]},{"zhname":["奥斯曼帝国六百年"],"enname":["Ottoman Centuries: The Rise and Fall of the Turkish Empire"],"year":["2018"],"score":["8.8"],"abstract":["奥斯曼帝国六百年，是三百年的强盛加上三百年的衰落的故事"],"size":["20MB"],"poster":["http://111.161.65.188:180/resources/s29895041.jpg"],"tags":[{"tag":["中文","英文","多语言"]}],"url":["magnet:?xt=urn:btih:2451a5157f5dd466b2eb9280ae3a48fb51eceb4c"]}]
     */

    private $Bean $;
    private List<ItemBean> ITEM;

    public $Bean get$() {
        return $;
    }

    public void set$($Bean $) {
        this.$ = $;
    }

    public List<ItemBean> getITEM() {
        return ITEM;
    }

    public void setITEM(List<ItemBean> ITEM) {
        this.ITEM = ITEM;
    }

}