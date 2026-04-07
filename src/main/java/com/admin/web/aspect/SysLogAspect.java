package com.admin.web.aspect;

import com.admin.web.annotation.SysLog;
import com.admin.web.model.SysUserLog;
import com.admin.web.service.SysUserLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

/**
 * @author znn
 */
@Aspect
@Component
public class SysLogAspect {
    private static final Logger log = LoggerFactory.getLogger(SysLogAspect.class);
    private static final ThreadLocal<Long> THREAD_LOCAL = new NamedThreadLocal<>("ms");
    private final SysUserLogService sysUserLogService;

    public SysLogAspect(SysUserLogService sysUserLogService) {
        this.sysUserLogService = sysUserLogService;
    }

    @Before(value = "@annotation(sysLog)")
    public void doBefore(JoinPoint joinPoint, SysLog sysLog) {
        log.info("SYS-ASPECT => [{}] {}.{}()", sysLog.value(), joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
        THREAD_LOCAL.set(System.currentTimeMillis());
    }

    @AfterReturning(pointcut = "@annotation(sysLog)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, SysLog sysLog, Object result) {
        this.doAround(sysLog, joinPoint.getArgs(), result, null);
    }

    @AfterThrowing(value = "@annotation(sysLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, SysLog sysLog, Exception e) {
        this.doAround(sysLog, joinPoint.getArgs(), null, e);
    }

    private void doAround(SysLog sysLog, Object[] args, Object result, Exception e) {
        try {
            SysUserLog logs = this.sysUserLogService.log(sysLog, args, result, e, THREAD_LOCAL.get());
            log.info("SYS-REQUEST => {}", logs.getParams());
            log.info("SYS-RESPONSE => {}", logs.getResult());
        } finally {
            THREAD_LOCAL.remove();
        }
    }
}
