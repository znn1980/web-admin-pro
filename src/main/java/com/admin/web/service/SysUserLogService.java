package com.admin.web.service;

import com.admin.web.dao.SysUserLogDao;
import com.admin.web.model.SysUserLog;
import com.admin.web.model.UserLogQuery;

import com.admin.web.utils.Specifications;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author znn
 */
@Service
public class SysUserLogService {
    private final SysUserLogDao sysUserLogDao;

    public SysUserLogService(SysUserLogDao sysUserLogDao) {
        this.sysUserLogDao = sysUserLogDao;
    }

    public Page<SysUserLog> findAll(UserLogQuery query, Pageable page) {
        if (!StringUtils.hasText(query.getUsername())) {
            query.setUsername(null);
        }
        return this.sysUserLogDao.findAll(Specifications.<SysUserLog>where()
                .equal("username", query.getUsername())
                .between("timestamp", query.getStartTimestamp(), query.getEndTimestamp())
                .orderBy("timestamp", false).build(), page);
    }


    public void deleteAllById(List<Long> id) {
        this.sysUserLogDao.deleteAllById(id);
    }

    public void save(SysUserLog sysUserLog) {
        this.sysUserLogDao.save(sysUserLog);
    }
}
