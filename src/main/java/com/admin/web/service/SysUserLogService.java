package com.admin.web.service;

import com.admin.web.annotation.SysLog;
import com.admin.web.model.request.PageRequest;
import com.admin.web.repository.SysUserLogRepository;
import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.entity.SysUserLog;
import com.admin.web.model.request.UserLogRequest;

import com.admin.web.utils.ExceptionUtils;
import com.admin.web.utils.SecurityUtils;
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
    private final SysUserLogRepository sysUserLogRepository;

    public SysUserLogService(SysUserLogRepository sysUserLogRepository) {
        this.sysUserLogRepository = sysUserLogRepository;
    }

    public Page<SysUserLog> all(UserLogRequest request) {
        return this.sysUserLogRepository.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(request.username())) {
                predicates.add(builder.equal(root.get("username"), request.username()));
            }
            if (Objects.nonNull(request.startTimestamp()) && Objects.nonNull(request.endTimestamp())) {
                predicates.add(builder.between(root.get("timestamp"), request.startTimestamp(), request.endTimestamp()));
            } else if (Objects.nonNull(request.startTimestamp())) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("timestamp"), request.startTimestamp()));
            } else if (Objects.nonNull(request.endTimestamp())) {
                predicates.add(builder.lessThanOrEqualTo(root.get("timestamp"), request.endTimestamp()));
            }
            return Objects.requireNonNull(query)
                    .where(predicates.toArray(new Predicate[0]))
                    .orderBy(builder.desc(root.get("timestamp")))
                    .getRestriction();
        }, PageRequest.of(request.page(), request.limit(), request.sort()));
    }

    public void delete(List<Long> id) {
        this.sysUserLogRepository.deleteAllById(id);
    }

    public SysUserLog log(SysLog sysLog, Object[] args, Object result, Throwable e, long ms) {
        SysUserLog logs = new SysUserLog();
        logs.setUsername(Objects.requireNonNullElse(
                SecurityUtils.getSysUser(WebUtils.getRequest()),
                SecurityUtils.getSuperAdmin()
        ).getUsername());
        logs.setIp(WebUtils.getClientIp(WebUtils.getRequest()));
        logs.setOs(WebUtils.getOs(WebUtils.getUserAgent(WebUtils.getRequest())));
        logs.setBrowser(WebUtils.getBrowser(WebUtils.getUserAgent(WebUtils.getRequest())));
        logs.setMethod(WebUtils.getRequest().getMethod());
        logs.setUrl(WebUtils.getRequest().getRequestURI());
        logs.setName(sysLog.value());
        if (Objects.nonNull(args)) {
            logs.setParams(this.getParams(args));
        }
        if (Objects.nonNull(result)) {
            logs.setResult(this.getDisplayString(result));
        }
        if (Objects.nonNull(e)) {
            if (e instanceof ServerResponseException ex) {
                logs.setResult(this.getDisplayString(ex.getServerResponse()));
            } else {
                logs.setErrors(ExceptionUtils.getStackTrace(e));
            }
        }
        logs.setMs(System.currentTimeMillis() - ms);
        logs.setTimestamp(LocalDateTime.now());
        return this.sysUserLogRepository.save(logs);
    }

    String getParams(Object[] args) {
        StringJoiner params = new StringJoiner(System.lineSeparator());
        Arrays.asList(args).forEach(arg -> {
            if (Objects.nonNull(arg) && !(arg instanceof HttpServletRequest
                    || arg instanceof HttpServletResponse)) {
                if (arg instanceof MultipartFile[] files) {
                    Arrays.asList(files).forEach(file ->
                            params.add(this.getDisplayString(file)));
                } else {
                    params.add(this.getDisplayString(arg));
                }

            }
        });
        return params.toString();
    }

    String getDisplayString(Object obj) {
        if (obj instanceof MultipartFile file) {
            return String.format("MultipartFile{name=%s, originalFilename=%s, contentType=%s}"
                    , file.getName(), file.getOriginalFilename(), file.getContentType());
        }
        return ObjectUtils.getDisplayString(obj);
    }
}
