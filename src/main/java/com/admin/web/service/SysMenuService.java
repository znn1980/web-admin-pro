package com.admin.web.service;

import com.admin.web.repository.SysMenuRepository;
import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.SysMenu;
import com.admin.web.model.SysUser;
import com.admin.web.model.enums.Move;
import com.admin.web.model.enums.ResponseCode;
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
    private final SysMenuRepository sysMenuRepository;

    public SysMenuService(SysMenuRepository sysMenuRepository) {
        this.sysMenuRepository = sysMenuRepository;
    }

    public List<SysMenu> all() {
        return this.sysMenuRepository.findByOrderBySort();
    }

    public List<SysMenu> all(SysUser sysUser) {
        if (SecurityUtils.isSuperAdmin(sysUser)) {
            //超级管理员可以看到所有菜单
            return this.sysMenuRepository.findBySysMenuOrderBySort(true);
        }
        if (SecurityUtils.isSysAdmin(sysUser)) {
            //普通管理员可以看到所有权限菜单，但是禁用的菜单任然不能操作
            return this.sysMenuRepository.findByUserIdOrderBySort(sysUser.getId());
        }
        //普通用户可以看到所有权限并且没有禁用的菜单
        return this.sysMenuRepository.findByUserIdAndDisableOrderBySort(sysUser.getId(), false);
    }

    @Transactional(rollbackFor = Exception.class)
    public void move(MoveVo vo) {
        SysMenu sysMenu = this.sysMenuRepository.findById(vo.id())
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        List<SysMenu> sysMenus = this.sysMenuRepository.findByPidOrderBySort(sysMenu.getPid());
        int index = IntStream.range(0, sysMenus.size())
                .filter(i -> Objects.equals(sysMenu.getId(), sysMenus.get(i).getId()))
                .findFirst().orElse(-1);
        boolean isUp = Objects.equals(Move.UP, vo.move());
        if (index == -1 || (isUp ? index <= 0 : index >= sysMenus.size() - 1)) {
            return;
        }
        SysMenu oldSysMenu = sysMenus.get(isUp ? index - 1 : index + 1);
        Long oldSysMenuSort = oldSysMenu.getSort();
        oldSysMenu.setSort(sysMenu.getSort());
        sysMenu.setSort(oldSysMenuSort);
        this.sysMenuRepository.saveAll(Arrays.asList(sysMenu, oldSysMenu));
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(SysMenu sysMenu) {
        if (Objects.nonNull(sysMenu.getPid())
                && !this.sysMenuRepository.existsById(sysMenu.getPid())) {
            throw new ServerResponseException("上级菜单不存在！");
        }
        if (Objects.nonNull(this.sysMenuRepository.findByTitle(sysMenu.getTitle()))) {
            throw new ServerResponseException("菜单标题已存在！");
        }
        this.sysMenuRepository.save(sysMenu);
        sysMenu.setSort(sysMenu.getId());
        this.sysMenuRepository.save(sysMenu);
    }

    public void update(SysMenu sysMenu) {
        SysMenu oldSysMenu = this.sysMenuRepository.findById(sysMenu.getId())
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (Objects.equals(sysMenu.getId(), sysMenu.getPid())) {
            throw new ServerResponseException("不能选择自己作为上级菜单！");
        }
        if (Objects.nonNull(sysMenu.getPid())
                && this.exists(sysMenu.getId(), sysMenu.getPid())) {
            throw new ServerResponseException("不能选择自己的下级菜单作为上级菜单！");
        }
        if (Objects.nonNull(sysMenu.getPid())
                && !this.sysMenuRepository.existsById(sysMenu.getPid())) {
            throw new ServerResponseException("上级菜单不存在！");
        }
        if (!Objects.equals(oldSysMenu.getTitle(), sysMenu.getTitle())
                && Objects.nonNull(this.sysMenuRepository.findByTitle(sysMenu.getTitle()))) {
            throw new ServerResponseException("菜单标题已存在！");
        }
        oldSysMenu.setPid(sysMenu.getPid());
        BeanUtils.copyNonNullProperties(sysMenu, oldSysMenu);
        this.sysMenuRepository.save(oldSysMenu);
    }

    public void delete(Long id) {
        SysMenu sysMenu = this.sysMenuRepository.findById(id)
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (this.sysMenuRepository.existsByMenuId(sysMenu.getId())) {
            throw new ServerResponseException("此菜单已绑定角色，请先解绑角色下的菜单！");
        }
        if (this.sysMenuRepository.existsByPid(sysMenu.getId())) {
            throw new ServerResponseException("请先删除下级菜单！");
        }
        this.sysMenuRepository.deleteById(id);
    }

    private boolean exists(Long id, Long pid) {
        Set<Long> exists = new HashSet<>();
        SysMenu sysMenu = this.sysMenuRepository.findById(pid).orElse(null);
        while (Objects.nonNull(sysMenu) && Objects.nonNull(sysMenu.getPid())) {
            exists.add(sysMenu.getPid());
            sysMenu = this.sysMenuRepository.findById(sysMenu.getPid()).orElse(null);
        }
        return exists.contains(id);
    }
}
