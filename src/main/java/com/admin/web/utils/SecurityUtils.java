package com.admin.web.utils;

import com.admin.web.model.entity.SysUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author znn
 */
public class SecurityUtils {
    static final Long SYS_USER_ID = 1L;
    static final String SYS_USER_NAME = "admin";
    static final String SYS_USER_SESSION = "SYS_USER_SESSION";
    static final String SYS_CAPTCHA_SESSION = "SYS_CAPTCHA_SESSION";

    private SecurityUtils() {
    }

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

    public static String getSysCaptcha(HttpServletRequest request) {
        return WebUtils.getSession(request, SYS_CAPTCHA_SESSION, String.class);
    }

    public static void setSysCaptcha(HttpServletRequest request, String captcha) {
        WebUtils.setSession(request, SYS_CAPTCHA_SESSION, captcha);
    }

    public static boolean hasPassword(SysUser sysUser, String password) {
        return Objects.nonNull(sysUser) && Objects.equals(sysUser.getPassword(), hexPassword(sysUser, password));
    }

    public static String hexPassword(SysUser sysUser, String password) {
        return DigestUtils
                .md5DigestAsHex(StringUtils
                        .arrayToCommaDelimitedString(new Object[]{sysUser.getId(), sysUser.getUsername(), password})
                        .getBytes());
    }

    public static String hexPassword(SysUser sysUser) {
        if (hasPassword(sysUser, sysUser.getPassword())) {
            return sysUser.getPassword();
        }
        return hexPassword(sysUser, sysUser.getPhone().substring(sysUser.getPhone().length() - 6));
    }
}
