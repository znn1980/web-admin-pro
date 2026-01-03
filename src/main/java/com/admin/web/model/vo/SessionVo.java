package com.admin.web.model.vo;

import java.io.Serializable;

/**
 * @author znn
 */
public class SessionVo implements Serializable {
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
        return "Session{" +
                "jsessionid='" + this.getJsessionid() + '\'' +
                '}';
    }
}
