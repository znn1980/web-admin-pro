package com.admin.web.aspect;

import com.admin.web.model.WebServerException;
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

import java.util.Arrays;
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
        log.info("ASPECT:{}.{}()", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
        SysUser sysUser = Optional.ofNullable(this.getSysUser()).orElseThrow(() -> {
            throw new WebServerException(ResponseCode.LOGOUT);
        });
        if (!SecurityUtils.isSysAdmin(sysUser)
                && !Arrays.asList(sysPermissions.value()).contains(SysLogin.class)) {
            log.info("判断权限=>{}:{} {}", WebUtils.getRequest().getMethod(), WebUtils.getRequest().getRequestURI(), sysUser);
        }
    }

    public SysUser getSysUser() {
        return SecurityUtils.getSysUser(WebUtils.getRequest());
    }
}
