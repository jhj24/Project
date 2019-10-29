package com.zgdj.lib.bean;


public class FileBean {
    //todo 实际应用中，图片路径需要格式化
    private int key;
    private String filename;
    private String src;
    private String url;

    public FileBean(String filename, String src) {
        this.filename = filename;
        this.src = src;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSrc() {
        return src == null ? url : src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getUrl() {
        return url == null ? src : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
