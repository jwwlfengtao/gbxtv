package com.xrq.tv.bean;

import java.util.List;

public class AllTaskBean {

    /**
     * code : 0
     * data : [{"magnet":"magnet:?xt=urn:btih:574c8114d16aa4dd61954b193fbf5f0be6375977","infoHash":"574c8114d16aa4dd61954b193fbf5f0be6375977","state":10,"_id":"hdjlRpooRMYiBRzK","metadata":[{"path":"somebody.mp4","length":21396761,"name":"somebody.mp4"}]}]
     */

    private int code;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * magnet : magnet:?xt=urn:btih:574c8114d16aa4dd61954b193fbf5f0be6375977
         * infoHash : 574c8114d16aa4dd61954b193fbf5f0be6375977
         * state : 10
         * _id : hdjlRpooRMYiBRzK
         * metadata : [{"path":"somebody.mp4","length":21396761,"name":"somebody.mp4"}]
         */
        private double uploadSpeed;
        private double downloadSpeed;
        private String magnet;
        private String infoHash;
        private int state;
        private String state_cn;
        private String _id;
        private double progress;
        private List<MetadataBean> metadata;

        public String getState_cn() {
            return state_cn;
        }

        public void setState_cn(String state_cn) {
            this.state_cn = state_cn;
        }

        public double getUploadSpeed() {
            return uploadSpeed;
        }

        public void setUploadSpeed(double uploadSpeed) {
            this.uploadSpeed = uploadSpeed;
        }

        public double getDownloadSpeed() {
            return downloadSpeed;
        }

        public void setDownloadSpeed(double downloadSpeed) {
            this.downloadSpeed = downloadSpeed;
        }

        public double getProgress() {
            return progress;
        }

        public void setProgress(double progress) {
            this.progress = progress;
        }

        public String getMagnet() {
            return magnet;
        }

        public void setMagnet(String magnet) {
            this.magnet = magnet;
        }

        public String getInfoHash() {
            return infoHash;
        }

        public void setInfoHash(String infoHash) {
            this.infoHash = infoHash;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public List<MetadataBean> getMetadata() {
            return metadata;
        }

        public void setMetadata(List<MetadataBean> metadata) {
            this.metadata = metadata;
        }

        public static class MetadataBean {
            /**
             * path : somebody.mp4
             * length : 21396761
             * name : somebody.mp4
             */

            private String path;
            private long length;
            private String name;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public long getLength() {
                return length;
            }

            public void setLength(long length) {
                this.length = length;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
