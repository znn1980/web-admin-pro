package com.admin.web.aspect;

import com.admin.web.annotation.SysLog;
import com.admin.web.model.entity.SysUserLog;
import com.admin.web.service.SysUserLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author znn
 */
@Aspect
@Component
public class SysLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(SysLogAspect.class);
    private final SysUserLogService sysUserLogService;

    public SysLogAspect(SysUserLogService sysUserLogService) {
        this.sysUserLogService = sysUserLogService;
    }

    @Around("@annotation(sysLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, SysLog sysLog) throws Throwable {
        logger.info("SYS-ASPECT => [{}] {}.{}()", sysLog.value()
                , joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
        long ms = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            this.log(sysLog, joinPoint.getArgs(), result, null, ms);
            return result;
        } catch (Throwable e) {
            this.log(sysLog, joinPoint.getArgs(), null, e, ms);
            throw e;
        }
    }

    void log(SysLog sysLog, Object[] args, Object result, Throwable e, long ms) {
        SysUserLog log = this.sysUserLogService.log(sysLog, args, result, e, ms);
        logger.info("SYS-REQUEST => {}", log.getParams());
        logger.info("SYS-RESPONSE => {}", log.getResult());
    }
}
