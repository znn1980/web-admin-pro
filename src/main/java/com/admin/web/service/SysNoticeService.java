package com.admin.web.service;

import com.admin.web.dao.SysNoticeDao;
import org.springframework.stereotype.Service;

/**
 * @author znn
 */
@Service
public class SysNoticeService {
    private final SysNoticeDao sysNoticeDao;

    public SysNoticeService(SysNoticeDao sysNoticeDao) {
        this.sysNoticeDao = sysNoticeDao;
    }
}
