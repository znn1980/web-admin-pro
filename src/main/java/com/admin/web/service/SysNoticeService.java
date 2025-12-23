package com.admin.web.service;

import com.admin.web.dao.SysNoticeDao;
import com.admin.web.model.SysNotice;
import com.admin.web.model.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author znn
 */
@Service
public class SysNoticeService {
    private final SysNoticeDao sysNoticeDao;

    public SysNoticeService(SysNoticeDao sysNoticeDao) {
        this.sysNoticeDao = sysNoticeDao;
    }

    public Page<SysNotice> findAll(Pageable page) {
        return this.sysNoticeDao.findAll(page);
    }

    public Optional<SysNotice> findById(Long id) {
        return this.sysNoticeDao.findById(id);
    }

    public void save(SysNotice sysNotice, SysUser sysUser) {
        Set<SysUser> sysUsers = sysNotice.getUsers();
        if (Objects.nonNull(sysUsers) && !sysUsers.contains(sysUser)) {
            sysUsers.add(sysUser);
            this.sysNoticeDao.save(sysNotice);
        }
    }

    public void save(SysNotice sysNotice) {
        this.sysNoticeDao.save(sysNotice);
    }

    public void deleteById(Long id) {
        this.sysNoticeDao.deleteById(id);
    }
}
