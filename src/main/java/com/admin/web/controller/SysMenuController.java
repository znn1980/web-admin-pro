package com.admin.web.controller;

import com.admin.web.annotation.*;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysMenu;
import com.admin.web.model.WebServerException;
import com.admin.web.model.vo.MoveVo;
import com.admin.web.service.SysMenuService;
import com.admin.web.utils.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author znn
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {
    private final SysMenuService sysMenuService;

    public SysMenuController(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    @SysPermissions(SysLogin.class)
    @GetMapping
    public ServerResponseEntity<List<SysMenu>> menus() {
        if (super.isSuperAdmin()) {
            //超级管理员可以看到所有菜单
            return ServerResponseEntity.ok(this.sysMenuService.findBySysMenuOrderBySort());
        }
        if (super.isSysAdmin()) {
            //普通管理员可以看到所有权限菜单，但是禁用的菜单任然不能操作
            return ServerResponseEntity.ok(this.sysMenuService.findByUserIdOrderBySort(super.getSysUser().getId()));
        }
        //普通用户可以看到所有权限并且没有禁用的菜单
        return ServerResponseEntity.ok(this.sysMenuService.findByUserIdAndEnableOrderBySort(super.getSysUser().getId()));
    }

    @SysPermissions
    @GetMapping("/all")
    public ServerResponseEntity<List<SysMenu>> all() {
        return ServerResponseEntity.ok(this.sysMenuService.findAllByOrderBySort());
    }

    @SysLog("移动菜单")
    @SysPermissions
    @PutMapping("/move")
    public ServerResponseEntity<?> move(@RequestBody MoveVo moveVo) {
        SysMenu sysMenu = this.sysMenuService.findById(moveVo.getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("菜单不存在！")));
        this.sysMenuService.move(sysMenu, moveVo.getMove());
        return ServerResponseEntity.ok();
    }

    @SysLog("添加菜单")
    @SysPermissions
    @PostMapping
    public ServerResponseEntity<?> save(@RequestBody @Validated(SysCreate.class) SysMenu sysMenu) {
        if (Objects.nonNull(sysMenu.getPid())
                && !this.sysMenuService.existsById(sysMenu.getPid())) {
            return ServerResponseEntity.fail("上级菜单不存在！");
        }
        if (Objects.nonNull(this.sysMenuService.findByTitle(sysMenu.getTitle()))) {
            return ServerResponseEntity.fail("菜单标题已存在！");
        }
        this.sysMenuService.save(sysMenu);
        return ServerResponseEntity.ok();
    }

    @SysLog("修改菜单")
    @SysPermissions
    @PutMapping
    public ServerResponseEntity<?> edit(@RequestBody @Validated(SysUpdate.class) SysMenu sysMenu) {
        SysMenu oldSysMenu = this.sysMenuService.findById(sysMenu.getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("菜单不存在！")));
        if (Objects.equals(sysMenu.getId(), sysMenu.getPid())) {
            return ServerResponseEntity.fail("不能选择自己作为上级菜单！");
        }
        if (Objects.nonNull(sysMenu.getPid())
                && this.sysMenuService.existsByIdAndPid(sysMenu.getId(), sysMenu.getPid())) {
            return ServerResponseEntity.fail("不能选择自己的下级菜单作为上级菜单！");
        }
        if (Objects.nonNull(sysMenu.getPid())
                && !this.sysMenuService.existsById(sysMenu.getPid())) {
            return ServerResponseEntity.fail("上级菜单不存在！");
        }
        if (!Objects.equals(oldSysMenu.getTitle(), sysMenu.getTitle())
                && Objects.nonNull(this.sysMenuService.findByTitle(sysMenu.getTitle()))) {
            return ServerResponseEntity.fail("菜单标题已存在！");
        }
        oldSysMenu.setPid(sysMenu.getPid());
        BeanUtils.copyNonNullProperties(sysMenu, oldSysMenu);
        this.sysMenuService.save(oldSysMenu);
        return ServerResponseEntity.ok();
    }

    @SysLog("删除菜单")
    @SysPermissions
    @DeleteMapping
    public ServerResponseEntity<?> delete(@RequestBody Long id) {
        if (this.sysMenuService.existsByMenuId(id)) {
            return ServerResponseEntity.fail("此菜单已绑定角色，请先解绑角色下的菜单！");
        }
        if (this.sysMenuService.existsByPid(id)) {
            return ServerResponseEntity.fail("请先删除下级菜单！");
        }
        this.sysMenuService.deleteById(id);
        return ServerResponseEntity.ok();
    }
}
