package com.admin.web.controller;

import com.admin.web.model.SysUser;
import com.admin.web.utils.SecurityUtils;
import com.admin.web.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author znn
 */
public class BaseController {

    public HttpServletRequest getRequest() {
        return WebUtils.getRequest();
    }

    public String getSessionId() {
        return WebUtils.getSessionId(this.getRequest());
    }

    public String getClientIp() {
        return WebUtils.getClientIp(this.getRequest());
    }

    public SysUser getSysUser() {
        return SecurityUtils.getSysUser(this.getRequest());
    }

    public void setSysUser(SysUser sysUser) {
        SecurityUtils.setSysUser(this.getRequest(), sysUser);
    }

    public String getSysCode() {
        return SecurityUtils.getSysCode(this.getRequest());
    }

    public void setSysCode(String sysCode) {
        SecurityUtils.setSysCode(this.getRequest(), sysCode);
    }

    public String hexPassword(String password) {
        return SecurityUtils.hexPassword(password);
    }

}
