package com.admin.web.aspect;

import com.admin.web.annotation.SysPermissions;
import com.admin.web.service.SysUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author znn
 */
@Aspect
@Component
public class SysPermissionsAspect {
    private static final Logger logger = LoggerFactory.getLogger(SysPermissionsAspect.class);
    private final SysUserService sysUserService;

    public SysPermissionsAspect(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Before(value = "@annotation(sysPermissions)")
    public void doBefore(JoinPoint joinPoint, SysPermissions sysPermissions) {
        logger.info("SYS-ASPECT => {}.{}()", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
        this.sysUserService.hasPermissions(sysPermissions);
    }
}
