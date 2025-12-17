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

    public boolean isSysAdmin() {
        return this.isSysAdmin(this.getSysUser());
    }

    public boolean isSysAdmin(SysUser sysUser) {
        return SecurityUtils.isSysAdmin(sysUser);
    }

    public boolean isSuperAdmin() {
        return this.isSuperAdmin(this.getSysUser());
    }

    public boolean isSuperAdmin(SysUser sysUser) {
        return SecurityUtils.isSuperAdmin(sysUser);
    }

    public String hexPassword(String password) {
        return SecurityUtils.hexPassword(password);
    }

    public String hexPassword(SysUser sysUser) {
        return this.hexPassword(sysUser.getMobile().substring(sysUser.getMobile().length() - 6));
    }

}
