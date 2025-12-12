package com.admin.web.model.vo;

/**
 * @author znn
 */
public class SessionVo {
    public String jsessionid;

    public SessionVo(String jsessionid) {
        this.setJsessionid(jsessionid);
    }

    public String getJsessionid() {
        return jsessionid;
    }

    public void setJsessionid(String jsessionid) {
        this.jsessionid = jsessionid;
    }

    @Override
    public String toString() {
        return "SessionVo{" +
                "jsessionid='" + this.getJsessionid() + '\'' +
                '}';
    }
}
