package com.admin.web.aspect;

import com.admin.web.annotation.SysLog;
import com.admin.web.model.SysUser;
import com.admin.web.model.SysUserLog;
import com.admin.web.service.SysUserLogService;
import com.admin.web.utils.SecurityUtils;
import com.admin.web.utils.UserAgentUtils;
import com.admin.web.utils.WebUtils;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

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
        log.info("ASPECT:{}.{}()", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
        THREAD_LOCAL.set(System.currentTimeMillis());
    }

    @AfterReturning(pointcut = "@annotation(sysLog)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, SysLog sysLog, Object result) {
        this.doAround(joinPoint, sysLog, result, null);
    }

    @AfterThrowing(value = "@annotation(sysLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, SysLog sysLog, Exception e) {
        this.doAround(joinPoint, sysLog, null, e);
    }

    void doAround(JoinPoint joinPoint, SysLog sysLog, Object result, Exception e) {
        try {
            SysUserLog sysUserLog = new SysUserLog();
            if (Objects.nonNull(joinPoint.getArgs())) {
                StringJoiner params = new StringJoiner(System.lineSeparator());
                Arrays.asList(joinPoint.getArgs()).forEach(arg -> {
                    if (Objects.nonNull(arg) && !this.filterObject(arg)) {
                        log.info("REQUEST:{}", arg);
                        params.add(arg.toString());
                    }
                });
                sysUserLog.setParams(params.toString());
            }
            if (Objects.nonNull(result)) {
                log.info("RESPONSE:{}", result);
                sysUserLog.setResult(result.toString());
            }
            if (Objects.nonNull(e)) {
                sysUserLog.setErrors(new StringWriter() {{
                    e.printStackTrace(new PrintWriter(this, true));
                }}.toString());
            }
            UserAgent userAgent = UserAgentUtils.getUserAgent(WebUtils.getRequest());
            sysUserLog.setUsername(this.getSysUser().getUsername());
            sysUserLog.setIp(WebUtils.getClientIp(WebUtils.getRequest()));
            sysUserLog.setOs(UserAgentUtils.getOs(userAgent));
            sysUserLog.setBrowser(UserAgentUtils.getBrowser(userAgent));
            sysUserLog.setMethod(WebUtils.getRequest().getMethod());
            sysUserLog.setUrl(WebUtils.getRequest().getRequestURI());
            sysUserLog.setName(sysLog.value());
            sysUserLog.setMs(System.currentTimeMillis() - THREAD_LOCAL.get());
            sysUserLog.setTimestamp(LocalDateTime.now());
            this.sysUserLogService.save(sysUserLog);
        } finally {
            THREAD_LOCAL.remove();
        }
    }

    SysUser getSysUser() {
        return Objects.requireNonNullElse(SecurityUtils.getSysUser(WebUtils.getRequest()), SecurityUtils.getSysUser());
    }

    boolean filterObject(Object o) {
        return o instanceof MultipartFile
                || o instanceof HttpServletRequest
                || o instanceof HttpServletResponse;
    }
}
