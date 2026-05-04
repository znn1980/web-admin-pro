package com.admin.web.controller;

import com.admin.web.model.entity.SysUser;
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

    public String getSysCaptcha() {
        return SecurityUtils.getSysCaptcha(this.getRequest());
    }

    public void setSysCaptcha(String sysCaptcha) {
        SecurityUtils.setSysCaptcha(this.getRequest(), sysCaptcha);
    }

}
