package com.admin.web.service;

import com.admin.web.annotation.SysLog;
import com.admin.web.dao.SysUserLogDao;
import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.SysUserLog;
import com.admin.web.model.vo.UserLogVo;

import com.admin.web.utils.ExceptionUtils;
import com.admin.web.utils.SecurityUtils;
import com.admin.web.utils.UserAgentUtils;
import com.admin.web.utils.WebUtils;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
            return Objects.requireNonNull(query)
                    .where(predicates.toArray(new Predicate[0]))
                    .orderBy(builder.desc(root.get("timestamp")))
                    .getRestriction();
        }, UserLogVo.of(vo));
    }

    public void delete(List<Long> id) {
        this.sysUserLogDao.deleteAllById(id);
    }

    public SysUserLog log(SysLog sysLog, Object[] args, Object result, Throwable e, long ms) {
        SysUserLog logs = new SysUserLog();
        logs.setUsername(Objects.requireNonNullElse(
                SecurityUtils.getSysUser(WebUtils.getRequest()),
                SecurityUtils.getSysUser()
        ).getUsername());
        logs.setIp(WebUtils.getClientIp(WebUtils.getRequest()));
        logs.setOs(UserAgentUtils.getOs(UserAgentUtils.getUserAgent(WebUtils.getRequest())));
        logs.setBrowser(UserAgentUtils.getBrowser(UserAgentUtils.getUserAgent(WebUtils.getRequest())));
        logs.setMethod(WebUtils.getRequest().getMethod());
        logs.setUrl(WebUtils.getRequest().getRequestURI());
        logs.setName(sysLog.value());
        if (Objects.nonNull(args)) {
            logs.setParams(this.getParams(args));
        }
        if (Objects.nonNull(result)) {
            logs.setResult(ObjectUtils.getDisplayString(result));
        }
        if (Objects.nonNull(e)) {
            if (e instanceof ServerResponseException ex) {
                logs.setResult(ObjectUtils.getDisplayString(ex.getServerResponse()));
            } else {
                logs.setErrors(ExceptionUtils.getStackTrace(e));
            }
        }
        logs.setMs(System.currentTimeMillis() - ms);
        logs.setTimestamp(LocalDateTime.now());
        return this.sysUserLogDao.save(logs);
    }

    private String getParams(Object[] args) {
        StringJoiner params = new StringJoiner(System.lineSeparator());
        Arrays.asList(args).forEach(arg -> {
            if (Objects.nonNull(arg) && !(arg instanceof HttpServletRequest
                    || arg instanceof HttpServletResponse)) {
                if (arg instanceof MultipartFile file) {
                    params.add(this.toMultipartFileString(file));
                } else if (arg instanceof MultipartFile[] files) {
                    Arrays.asList(files).forEach(file ->
                            params.add(this.toMultipartFileString(file)));
                } else {
                    params.add(ObjectUtils.getDisplayString(arg));
                }

            }
        });
        return params.toString();
    }

    private String toMultipartFileString(MultipartFile file) {
        return String.format("MultipartFile{name=%s, originalFilename=%s, contentType=%s}"
                , file.getName(), file.getOriginalFilename(), file.getContentType());
    }
}
