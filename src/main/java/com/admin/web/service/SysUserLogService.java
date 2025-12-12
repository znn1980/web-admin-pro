package com.admin.web.service;

import com.admin.web.dao.SysUserLogDao;
import com.admin.web.model.SysUserLog;
import com.admin.web.model.vo.UserLogVo;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author znn
 */
@Service
public class SysUserLogService {
    private final SysUserLogDao sysUserLogDao;

    public SysUserLogService(SysUserLogDao sysUserLogDao) {
        this.sysUserLogDao = sysUserLogDao;
    }

    public Page<SysUserLog> findAll(UserLogVo userLogVo, Pageable page) {
        return this.sysUserLogDao.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(userLogVo.getUsername())) {
                predicates.add(builder.equal(root.get("username"), userLogVo.getUsername()));
            }
            if (Objects.nonNull(userLogVo.getStartTimestamp()) && Objects.nonNull(userLogVo.getEndTimestamp())) {
                predicates.add(builder.between(root.get("timestamp"), userLogVo.getStartTimestamp(), userLogVo.getEndTimestamp()));
            } else if (Objects.nonNull(userLogVo.getStartTimestamp())) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("timestamp"), userLogVo.getStartTimestamp()));
            } else if (Objects.nonNull(userLogVo.getEndTimestamp())) {
                predicates.add(builder.lessThanOrEqualTo(root.get("timestamp"), userLogVo.getEndTimestamp()));
            }
            query.orderBy(builder.desc(root.get("timestamp")));
            return builder.and(predicates.toArray(new Predicate[0]));
        }, page);
    }


    public void deleteAllById(List<Long> id) {
        this.sysUserLogDao.deleteAllById(id);
    }

    public void save(SysUserLog sysUserLog) {
        this.sysUserLogDao.save(sysUserLog);
    }
}
