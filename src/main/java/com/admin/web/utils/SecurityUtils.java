package com.admin.web.utils;

import com.admin.web.model.SysUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.DigestUtils;

import java.util.Objects;

/**
 * @author znn
 */
public class SecurityUtils {
    static final Long SYS_USER_ID = 1L;

    static final String SYS_USER_NAME = "admin";
    static final String SYS_USER_SESSION = "SYS_USER_SESSION";
    static final String SYS_CODE_SESSION = "SYS_CODE_SESSION";
    static final String PASSWORD_SALT = "$%s$";

    public static SysUser getSysUser() {
        return new SysUser(SYS_USER_ID, SYS_USER_NAME);
    }

    public static SysUser getSysUser(HttpServletRequest request) {
        return WebUtils.getSession(request, SYS_USER_SESSION, SysUser.class);
    }

    public static void setSysUser(HttpServletRequest request, SysUser sysUser) {
        WebUtils.setSession(request, SYS_USER_SESSION, sysUser);
    }

    public static boolean isSysAdmin(SysUser sysUser) {
        return !Objects.isNull(sysUser) && (Objects.equals(SYS_USER_ID, sysUser.getId()) || sysUser.isSysAdmin());
    }

    public static String getSysCode(HttpServletRequest request) {
        return WebUtils.getSession(request, SYS_CODE_SESSION, String.class);
    }

    public static void setSysCode(HttpServletRequest request, String sysUserCode) {
        WebUtils.setSession(request, SYS_CODE_SESSION, sysUserCode);
    }

    public static String hexPassword(String password) {
        return DigestUtils.md5DigestAsHex(String.format(PASSWORD_SALT, password).getBytes());
    }
}
