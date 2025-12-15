package com.admin.web.service;

import com.admin.web.dao.SysMenuDao;
import com.admin.web.model.SysMenu;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author znn
 */
@Service
public class SysMenuService {
    private final SysMenuDao sysMenuDao;

    public SysMenuService(SysMenuDao sysMenuDao) {
        this.sysMenuDao = sysMenuDao;
    }

    public List<SysMenu> findAll() {
        return this.sysMenuDao.findAll();
    }

    public SysMenu findByTitle(String title) {
        return this.sysMenuDao.findByTitle(title);
    }


}
