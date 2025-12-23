package com.admin.web.model;

/**
 * @author znn
 */
public class SysUpload {
    private String src;
    private String title;

    public SysUpload() {
        this.setSrc(null);
        this.setTitle(null);
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "SysUpload{" +
                "src='" + this.getSrc() + '\'' +
                ", title='" + this.getTitle() + '\'' +
                '}';
    }
}
