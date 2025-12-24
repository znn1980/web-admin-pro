package com.admin.web.service;

import com.admin.web.dao.SysRoleDao;
import com.admin.web.model.SysRole;
import com.admin.web.model.enums.Move;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    public List<SysRole> findAll() {
        return this.sysRoleDao.findByOrderBySort();
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(SysRole sysRole) {
        if (Objects.nonNull(sysRole.getId())) {
            this.sysRoleDao.save(sysRole);
        } else {
            this.sysRoleDao.save(sysRole);
            sysRole.setSort(sysRole.getId());
            this.sysRoleDao.save(sysRole);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void move(SysRole sysRole, Move move) {
        List<SysRole> sysRoles = this.sysRoleDao.findByOrderBySort();
        int index = -1;
        for (int i = 0; i < sysRoles.size(); i++) {
            if (sysRoles.get(i).getId().equals(sysRole.getId())) {
                index = i;
                break;
            }
        }
        if (Move.UP == move ? index > 0 : index < sysRoles.size() - 1 && index != -1) {
            SysRole oldSysRole = sysRoles.get(Move.UP == move ? index - 1 : index + 1);
            Long oldSort = oldSysRole.getSort();
            oldSysRole.setSort(sysRole.getSort());
            sysRole.setSort(oldSort);
            this.sysRoleDao.save(sysRole);
            this.sysRoleDao.save(oldSysRole);
        }
    }

    public void deleteById(Long id) {
        this.sysRoleDao.deleteById(id);
    }

    public boolean existsByRoleId(Long roleId) {
        return this.sysRoleDao.existsByRoleId(roleId);
    }
}
