package com.admin.web.aspect;

import com.admin.web.WebServerException;
import com.admin.web.annotation.SysLogin;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.model.ResponseCode;
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
import java.util.Objects;

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
        SysUser sysUser = SecurityUtils.getSysUser(WebUtils.getRequest());
        if (Objects.isNull(sysUser)) {
            throw new WebServerException(ResponseCode.LOGOUT);
        }
        if (!Arrays.asList(sysPermissions.value()).contains(SysLogin.class)) {
            //判断权限
        }
    }
}
