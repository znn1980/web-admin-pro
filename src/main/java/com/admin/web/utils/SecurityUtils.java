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
    static final String SYS_PASSWORD_SALT = "$%s$";

    public static SysUser getSuperAdmin() {
        return new SysUser(SYS_USER_ID, SYS_USER_NAME);
    }

    public static SysUser getSysUser(HttpServletRequest request) {
        return WebUtils.getSession(request, SYS_USER_SESSION, SysUser.class);
    }

    public static void setSysUser(HttpServletRequest request, SysUser sysUser) {
        WebUtils.setSession(request, SYS_USER_SESSION, sysUser);
    }

    public static boolean hasSysAdmin(SysUser sysUser) {
        return Objects.nonNull(sysUser)
                && (hasSuperAdmin(sysUser) || sysUser.isSysAdmin());
    }

    public static boolean hasSuperAdmin(SysUser sysUser) {
        return Objects.nonNull(sysUser)
                && Objects.equals(SYS_USER_ID, sysUser.getId())
                && Objects.equals(SYS_USER_NAME, sysUser.getUsername());
    }

    public static String getSysCode(HttpServletRequest request) {
        return WebUtils.getSession(request, SYS_CODE_SESSION, String.class);
    }

    public static void setSysCode(HttpServletRequest request, String sysUserCode) {
        WebUtils.setSession(request, SYS_CODE_SESSION, sysUserCode);
    }

    public static boolean hasPassword(SysUser sysUser, String password) {
        return Objects.nonNull(sysUser) && Objects.equals(sysUser.getPassword(), hexPassword(password));
    }

    public static String hexPassword(String password) {
        return DigestUtils.md5DigestAsHex(String.format(SYS_PASSWORD_SALT, password).getBytes());
    }

    public static String hexPassword(SysUser sysUser) {
        if (hasPassword(sysUser, sysUser.getPassword())) {
            return sysUser.getPassword();
        }
        return hexPassword(sysUser.getPhone().substring(sysUser.getPhone().length() - 6));
    }
}
