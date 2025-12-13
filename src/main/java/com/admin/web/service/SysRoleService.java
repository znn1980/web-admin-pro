package com.admin.web.service;

import com.admin.web.dao.SysRoleDao;
import com.admin.web.model.SysRole;
import com.admin.web.model.enums.Move;
import jakarta.persistence.Transient;
import org.springframework.stereotype.Service;

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

    public List<SysRole> findByAll() {
        return this.sysRoleDao.findAllByOrderBySort();
    }

    @Transient
    public SysRole save(SysRole sysRole) {
        if (Objects.nonNull(sysRole.getId())) {
            return this.sysRoleDao.save(sysRole);
        }
        this.sysRoleDao.save(sysRole);
        sysRole.setSort(sysRole.getId());
        return this.sysRoleDao.save(sysRole);
    }

    @Transient
    public void move(SysRole sysRole, Move move) {
        List<SysRole> sysRoles = this.sysRoleDao.findAllByOrderBySort();
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
}
