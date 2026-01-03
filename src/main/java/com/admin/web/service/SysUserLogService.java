package com.admin.web.service;

import com.admin.web.annotation.SysLog;
import com.admin.web.dao.SysUserLogDao;
import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.SysUserLog;
import com.admin.web.model.vo.UserLogVo;

import com.admin.web.utils.SecurityUtils;
import com.admin.web.utils.UserAgentUtils;
import com.admin.web.utils.WebUtils;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author znn
 */
@Service
public class SysUserLogService {
    private final SysUserLogDao sysUserLogDao;

    public SysUserLogService(SysUserLogDao sysUserLogDao) {
        this.sysUserLogDao = sysUserLogDao;
    }

    public Page<SysUserLog> all(UserLogVo vo) {
        return this.sysUserLogDao.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(vo.getUsername())) {
                predicates.add(builder.equal(root.get("username"), vo.getUsername()));
            }
            if (Objects.nonNull(vo.getStartTimestamp()) && Objects.nonNull(vo.getEndTimestamp())) {
                predicates.add(builder.between(root.get("timestamp"), vo.getStartTimestamp(), vo.getEndTimestamp()));
            } else if (Objects.nonNull(vo.getStartTimestamp())) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("timestamp"), vo.getStartTimestamp()));
            } else if (Objects.nonNull(vo.getEndTimestamp())) {
                predicates.add(builder.lessThanOrEqualTo(root.get("timestamp"), vo.getEndTimestamp()));
            }
            query.orderBy(builder.desc(root.get("timestamp")));
            return builder.and(predicates.toArray(new Predicate[0]));
        }, UserLogVo.of(vo));
    }

    public void delete(List<Long> id) {
        this.sysUserLogDao.deleteAllById(id);
    }

    public SysUserLog log(SysLog sysLog, Object[] args, Object result, Exception error, long ms) {
        SysUserLog sysUserLog = new SysUserLog();
        sysUserLog.setUsername(Objects.requireNonNullElse(SecurityUtils.getSysUser(WebUtils.getRequest())
                , SecurityUtils.getSysUser()).getUsername());
        sysUserLog.setIp(WebUtils.getClientIp(WebUtils.getRequest()));
        sysUserLog.setOs(UserAgentUtils.getOs(UserAgentUtils.getUserAgent(WebUtils.getRequest())));
        sysUserLog.setBrowser(UserAgentUtils.getBrowser(UserAgentUtils.getUserAgent(WebUtils.getRequest())));
        sysUserLog.setMethod(WebUtils.getRequest().getMethod());
        sysUserLog.setUrl(WebUtils.getRequest().getRequestURI());
        sysUserLog.setName(sysLog.value());
        if (Objects.nonNull(args)) {
            sysUserLog.setParams(this.getParams(args));
        }
        if (Objects.nonNull(result)) {
            sysUserLog.setResult(Objects.toString(result));
        }
        if (Objects.nonNull(error)) {
            if (error instanceof ServerResponseException e) {
                sysUserLog.setResult(Objects.toString(e.getServerResponse()));
            } else {
                sysUserLog.setErrors(new StringWriter() {{
                    error.printStackTrace(new PrintWriter(this, true));
                }}.toString());
            }
        }
        sysUserLog.setMs(System.currentTimeMillis() - ms);
        sysUserLog.setTimestamp(LocalDateTime.now());
        return this.sysUserLogDao.save(sysUserLog);
    }

    private String getParams(Object[] args) {
        StringJoiner params = new StringJoiner(System.lineSeparator());
        Arrays.asList(args).forEach(arg -> {
            if (Objects.nonNull(arg) && !(arg instanceof MultipartFile
                    || arg instanceof HttpServletRequest
                    || arg instanceof HttpServletResponse)) {
                params.add(Objects.toString(arg));
            }
        });
        return params.toString();
    }

}
