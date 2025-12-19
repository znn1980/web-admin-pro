package com.admin.web.model.vo;


import java.io.Serializable;

/**
 * @author znn
 */
public class MappingVo implements Serializable {
    private String method;
    private String url;

    public MappingVo(String method, String url) {
        this.setMethod(method);
        this.setUrl(url);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "MappingVo{" +
                "method='" + this.getMethod() + '\'' +
                ", url='" + this.getUrl() + '\'' +
                '}';
    }
}
