package com.admin.web.service;

import com.admin.web.dao.SysMenuDao;
import com.admin.web.exception.WebServerException;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysMenu;
import com.admin.web.model.SysUser;
import com.admin.web.model.enums.Move;
import com.admin.web.model.vo.MoveVo;
import com.admin.web.utils.BeanUtils;
import com.admin.web.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author znn
 */
@Service
public class SysMenuService {
    private final SysMenuDao sysMenuDao;

    public SysMenuService(SysMenuDao sysMenuDao) {
        this.sysMenuDao = sysMenuDao;
    }

    public List<SysMenu> all() {
        return this.sysMenuDao.findByOrderBySort();
    }

    public List<SysMenu> all(SysUser sysUser) {
        if (SecurityUtils.isSuperAdmin(sysUser)) {
            //超级管理员可以看到所有菜单
            return this.sysMenuDao.findBySysMenuOrderBySort(true);
        }
        if (SecurityUtils.isSysAdmin(sysUser)) {
            //普通管理员可以看到所有权限菜单，但是禁用的菜单任然不能操作
            return this.sysMenuDao.findByUserIdOrderBySort(sysUser.getId());
        }
        //普通用户可以看到所有权限并且没有禁用的菜单
        return this.sysMenuDao.findByUserIdAndDisableOrderBySort(sysUser.getId(), false);
    }

    @Transactional(rollbackFor = Exception.class)
    public void move(MoveVo vo) {
        SysMenu sysMenu = this.sysMenuDao.findById(vo.getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("菜单不存在！")));
        List<SysMenu> sysMenus = this.sysMenuDao.findByPidOrderBySort(sysMenu.getPid());
        int index = IntStream.range(0, sysMenus.size())
                .filter(i -> Objects.equals(sysMenu.getId(), sysMenus.get(i).getId()))
                .boxed().findFirst().orElse(-1);
        if (Objects.equals(Move.UP, vo.getMove()) ? index > 0 : index < sysMenus.size() - 1 && index != -1) {
            SysMenu oldSysMenu = sysMenus.get(Objects.equals(Move.UP, vo.getMove()) ? index - 1 : index + 1);
            Long oldSysMenuSort = oldSysMenu.getSort();
            oldSysMenu.setSort(sysMenu.getSort());
            sysMenu.setSort(oldSysMenuSort);
            this.sysMenuDao.saveAll(Arrays.asList(sysMenu, oldSysMenu));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(SysMenu sysMenu) {
        if (Objects.nonNull(sysMenu.getPid())
                && !this.sysMenuDao.existsById(sysMenu.getPid())) {
            throw new WebServerException(ServerResponseEntity.fail("上级菜单不存在！"));
        }
        if (Objects.nonNull(this.sysMenuDao.findByTitle(sysMenu.getTitle()))) {
            throw new WebServerException(ServerResponseEntity.fail("菜单标题已存在！"));
        }
        this.sysMenuDao.save(sysMenu);
        sysMenu.setSort(sysMenu.getId());
        this.sysMenuDao.save(sysMenu);
    }

    public void update(SysMenu sysMenu) {
        SysMenu oldSysMenu = this.sysMenuDao.findById(sysMenu.getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("菜单不存在！")));
        if (Objects.equals(sysMenu.getId(), sysMenu.getPid())) {
            throw new WebServerException(ServerResponseEntity.fail("不能选择自己作为上级菜单！"));
        }
        if (Objects.nonNull(sysMenu.getPid())
                && this.existsByIdAndPid(sysMenu.getId(), sysMenu.getPid())) {
            throw new WebServerException(ServerResponseEntity.fail("不能选择自己的下级菜单作为上级菜单！"));
        }
        if (Objects.nonNull(sysMenu.getPid())
                && !this.sysMenuDao.existsById(sysMenu.getPid())) {
            throw new WebServerException(ServerResponseEntity.fail("上级菜单不存在！"));
        }
        if (!Objects.equals(oldSysMenu.getTitle(), sysMenu.getTitle())
                && Objects.nonNull(this.sysMenuDao.findByTitle(sysMenu.getTitle()))) {
            throw new WebServerException(ServerResponseEntity.fail("菜单标题已存在！"));
        }
        oldSysMenu.setPid(sysMenu.getPid());
        BeanUtils.copyNonNullProperties(sysMenu, oldSysMenu);
        this.sysMenuDao.save(oldSysMenu);
    }

    public void delete(Long id) {
        SysMenu sysMenu = this.sysMenuDao.findById(id)
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("菜单不存在！")));
        if (this.sysMenuDao.existsByMenuId(sysMenu.getId())) {
            throw new WebServerException(ServerResponseEntity.fail("此菜单已绑定角色，请先解绑角色下的菜单！"));
        }
        if (this.sysMenuDao.existsByPid(sysMenu.getId())) {
            throw new WebServerException(ServerResponseEntity.fail("请先删除下级菜单！"));
        }
        this.sysMenuDao.deleteById(id);
    }

    private boolean existsByIdAndPid(Long id, Long pid) {
        Set<Long> exists = new HashSet<>();
        SysMenu sysMenu = this.sysMenuDao.findById(pid).orElse(null);
        while (Objects.nonNull(sysMenu) && Objects.nonNull(sysMenu.getPid())) {
            exists.add(sysMenu.getPid());
            sysMenu = this.sysMenuDao.findById(sysMenu.getPid()).orElse(null);
        }
        return exists.contains(id);
    }
}
