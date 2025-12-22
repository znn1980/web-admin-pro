package com.admin.web.aspect;

import com.admin.web.model.SysMenu;
import com.admin.web.model.SysRole;
import com.admin.web.exception.WebServerException;
import com.admin.web.annotation.SysLogin;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.model.enums.ResponseCode;
import com.admin.web.model.SysUser;
import com.admin.web.utils.SecurityUtils;
import com.admin.web.utils.WebUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author znn
 */
@Aspect
@Component
public class SysPermissionsAspect {
    private static final Logger log = LoggerFactory.getLogger(SysPermissionsAspect.class);

    @Before(value = "@annotation(sysPermissions)")
    public void doBefore(JoinPoint joinPoint, SysPermissions sysPermissions) {
        log.info("SYS-ASPECT => {}.{}()", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
        SysUser sysUser = Optional.ofNullable(this.getSysUser()).orElseThrow(() -> {
            throw new WebServerException(ResponseCode.LOGOUT);
        });
        if (!SecurityUtils.isSuperAdmin(sysUser)
                && !Arrays.asList(sysPermissions.value()).contains(SysLogin.class)) {
            if (!this.hasPermission(sysUser)) {
                throw new WebServerException(ResponseCode.DENIED);
            }
        }
    }

    boolean hasPermission(SysUser sysUser) {
        PathMatcher matcher = new AntPathMatcher();
        for (SysRole sysRole : sysUser.getRoles()) {
            for (SysMenu sysMenu : sysRole.getMenus()) {
                log.info("SYS-PERMISSIONS => {}:{} => {}:{}", sysMenu.getMethod(), sysMenu.getUrl()
                        , WebUtils.getRequest().getMethod(), WebUtils.getRequest().getRequestURI());
                if (!sysMenu.isDisable() && StringUtils.hasText(sysMenu.getUrl())
                        && matcher.match(sysMenu.getUrl(), WebUtils.getRequest().getRequestURI())
                        && Objects.equals(sysMenu.getMethod(), WebUtils.getRequest().getMethod())) {
                    return true;
                }
            }
        }
        return false;
    }

    SysUser getSysUser() {
        return SecurityUtils.getSysUser(WebUtils.getRequest());
    }
}
