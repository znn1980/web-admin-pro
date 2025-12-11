package com.admin.web.service;

import com.admin.web.dao.SysRoleDao;
import com.admin.web.model.SysRole;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author znn
 */
@Service
public class SysRoleService {
    private final SysRoleDao sysRoleDao;

    public SysRoleService(SysRoleDao sysRoleDao) {
        this.sysRoleDao = sysRoleDao;
    }

    public SysRole findByName(String name) {
        return this.sysRoleDao.findByName(name);
    }

    public Optional<SysRole> findById(Long id) {
        return this.sysRoleDao.findById(id);
    }

    public List<SysRole> findByAll() {
        return this.sysRoleDao.findAll();
    }

    public SysRole save(SysRole sysRole) {
        return this.sysRoleDao.save(sysRole);
    }

    public void deleteById(Long id) {
        this.sysRoleDao.deleteById(id);
    }
}
