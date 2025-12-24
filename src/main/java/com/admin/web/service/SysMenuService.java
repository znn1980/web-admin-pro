package com.admin.web.service;

import com.admin.web.dao.SysMenuDao;
import com.admin.web.model.SysMenu;
import com.admin.web.model.SysUser;
import com.admin.web.model.enums.Move;
import com.admin.web.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        return this.sysMenuDao.findAllByOrderBySort();
    }

    public List<SysMenu> findAll(SysUser sysUser) {
        if (SecurityUtils.isSuperAdmin(sysUser)) {
            //超级管理员可以看到所有菜单
            return this.sysMenuDao.findBySysMenuOrderBySort(true);
        }
        if (SecurityUtils.isSysAdmin(sysUser)) {
            //普通管理员可以看到所有权限菜单，但是禁用的菜单任然不能操作
            return this.sysMenuDao.findByUserIdOrderBySort(sysUser.getId());
        }
        //普通用户可以看到所有权限并且没有禁用的菜单
        return this.sysMenuDao.findByUserIdAndEnableOrderBySort(sysUser.getId());
    }

    public SysMenu findByTitle(String title) {
        return this.sysMenuDao.findByTitle(title);
    }

    public Optional<SysMenu> findById(Long id) {
        return this.sysMenuDao.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(SysMenu sysMenu) {
        if (Objects.nonNull(sysMenu.getId())) {
            this.sysMenuDao.save(sysMenu);
        } else {
            this.sysMenuDao.save(sysMenu);
            sysMenu.setSort(sysMenu.getId());
            this.sysMenuDao.save(sysMenu);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void move(SysMenu sysMenu, Move move) {
        List<SysMenu> sysMenus = this.sysMenuDao.findByPidOrderBySort(sysMenu.getPid());
        int index = -1;
        for (int i = 0; i < sysMenus.size(); i++) {
            if (sysMenus.get(i).getId().equals(sysMenu.getId())) {
                index = i;
                break;
            }
        }
        if (Move.UP == move ? index > 0 : index < sysMenus.size() - 1 && index != -1) {
            SysMenu oldSysMenu = sysMenus.get(Move.UP == move ? index - 1 : index + 1);
            Long oldSort = oldSysMenu.getSort();
            oldSysMenu.setSort(sysMenu.getSort());
            sysMenu.setSort(oldSort);
            this.sysMenuDao.save(sysMenu);
            this.sysMenuDao.save(oldSysMenu);
        }
    }

    public void deleteById(Long id) {
        this.sysMenuDao.deleteById(id);
    }

    public boolean existsByMenuId(Long menuId) {
        return this.sysMenuDao.existsByMenuId(menuId);
    }

    public boolean existsByPid(Long pid) {
        return this.sysMenuDao.existsByPid(pid);
    }

    public boolean existsById(Long id) {
        return this.sysMenuDao.existsById(id);
    }

    public boolean existsByIdAndPid(Long id, Long pid) {
        Set<Long> exists = new HashSet<>();
        SysMenu sysMenu = this.sysMenuDao.findById(pid).orElse(null);
        while (Objects.nonNull(sysMenu) && Objects.nonNull(sysMenu.getPid())) {
            exists.add(sysMenu.getPid());
            sysMenu = this.sysMenuDao.findById(sysMenu.getPid()).orElse(null);
        }
        return exists.contains(id);
    }
}
