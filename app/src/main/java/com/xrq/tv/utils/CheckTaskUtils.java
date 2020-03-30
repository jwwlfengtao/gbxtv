package com.xrq.tv.utils;


import com.xrq.tv.bean.ItemBean;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 765773123
 * @date 2018/12/18
 * 检测文件
 */
public class CheckTaskUtils {

    private CheckTaskUtils() {

    }

    public static CheckTaskUtils getInstance() {
        return CheckTaskUtilsHolder.CHECK_TASK_UTILS;
    }

    private static class CheckTaskUtilsHolder {
        private static final CheckTaskUtils CHECK_TASK_UTILS = new CheckTaskUtils();
    }

    private boolean checkTask(String fileName) {
        String storePath = DataModel.getInstance().getStorePath();
        String filePath = storePath + "/" + fileName;
        File file = new File(filePath);
        return file.exists();
    }

    public void checkData(List<ItemBean> data) {
        if (data != null) {
            List<ItemBean> cachData = SharedPreferencesUtils.get("taskData");
            List<ItemBean> downData = SharedPreferencesUtils.get("haveDown");
            //把已下载和正在下载的数据与数据源匹配
            List<ItemBean> itemBeanList = new ArrayList<>();
            if (cachData != null) {
                for (int i = 0; i < cachData.size(); i++) {
                    for (ItemBean itemBean : data) {
                        //已添加但是未下载完成的文件
                        if (itemBean.getURL() != null && itemBean.getURL().equals(cachData.get(i).getURL()) && cachData.get(i).getState() == 0) {
                            itemBean.setAdd(true);
                            itemBean.setState(0);
                            itemBeanList.add(itemBean);
                        }
                    }
                }
            }
            if (downData != null) {
                for (int i = 0; i < downData.size(); i++) {
                    for (ItemBean itemBean : data) {
                        //已添加并且下载完成的文件
                        if (itemBean.getURL() != null && itemBean.getURL().equals(downData.get(i).getURL()) && downData.get(i).getState() == 1) {
                            itemBean.setAdd(true);
                            itemBean.setState(1);
                            itemBeanList.add(itemBean);

//                            //如果文件存在，则不能重复下载
//                            if (checkTask(cachData.get(i).getSaveFileName())) {
//                                itemBean.setAdd(true);
//                                itemBeanList.add(itemBean);
//                            } else {
//                                itemBean.setAdd(false);
//                            }
                        }
                    }
                }
            }
        }
    }
}
