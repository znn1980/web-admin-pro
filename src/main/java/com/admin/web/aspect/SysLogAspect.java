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
            UserAgent userAgent = UserAgentUtils.getUserAgent(WebUtils.getRequest());
            SysUserLog sysUserLog = new SysUserLog();
            sysUserLog.setUsername(getSysUser().getUsername());
            sysUserLog.setIp(WebUtils.getClientIp(WebUtils.getRequest()));
            sysUserLog.setOs(UserAgentUtils.getOs(userAgent));
            sysUserLog.setBrowser(UserAgentUtils.getBrowser(userAgent));
            sysUserLog.setMethod(WebUtils.getRequest().getMethod());
            sysUserLog.setUrl(WebUtils.getRequest().getRequestURI());
            sysUserLog.setName(sysLog.value());
            if (Objects.nonNull(joinPoint.getArgs())) {
                sysUserLog.setParams(getParams(joinPoint.getArgs()));
                log.info("REQUEST:{}", sysUserLog.getParams());
            }
            if (Objects.nonNull(result)) {
                sysUserLog.setResult(result.toString());
                log.info("RESPONSE:{}", sysUserLog.getResult());
            }
            if (Objects.nonNull(e)) {
                sysUserLog.setErrors(getStackTrace(e));
            }
            sysUserLog.setMs(System.currentTimeMillis() - THREAD_LOCAL.get());
            sysUserLog.setTimestamp(LocalDateTime.now());
            this.sysUserLogService.save(sysUserLog);
        } finally {
            THREAD_LOCAL.remove();
        }
    }

    static SysUser getSysUser() {
        SysUser sysUser = SecurityUtils.getSysUser(WebUtils.getRequest());
        return Objects.requireNonNullElse(sysUser, SecurityUtils.getSysUser());
    }

    static boolean filterObject(Object o) {
        return o instanceof MultipartFile
                || o instanceof HttpServletRequest
                || o instanceof HttpServletResponse;
    }

    static String getParams(Object[] args) {
        StringJoiner params = new StringJoiner(System.lineSeparator());
        Arrays.asList(args).forEach(arg -> {
            if (Objects.nonNull(arg) && !filterObject(arg)) {
                params.add(arg.toString());
            }
        });
        return params.toString();
    }

    static String getStackTrace(Exception e) {
        return new StringWriter() {{
            e.printStackTrace(new PrintWriter(this, true));
        }}.toString();
    }
}
