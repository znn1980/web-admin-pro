package com.admin.web.controller;

import com.admin.web.annotation.*;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysMenu;
import com.admin.web.model.vo.MoveVo;
import com.admin.web.service.SysMenuService;
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
    @GetMapping("/me")
    public ServerResponseEntity<List<SysMenu>> me() {
        List<SysMenu> sysMenus = this.sysMenuService.all(super.getSysUser());
        return ServerResponseEntity.ok(sysMenus);
    }

    @SysPermissions
    @GetMapping("/all")
    public ServerResponseEntity<List<SysMenu>> all() {
        List<SysMenu> sysMenus = this.sysMenuService.all();
        return ServerResponseEntity.ok(sysMenus);
    }

    @SysLog("移动菜单")
    @SysPermissions
    @PutMapping("/move")
    public ServerResponseEntity<?> move(@RequestBody MoveVo vo) {
        this.sysMenuService.move(vo);
        return ServerResponseEntity.ok();
    }

    @SysLog("创建菜单")
    @SysPermissions
    @PostMapping("/create")
    public ServerResponseEntity<?> create(@RequestBody @Validated(SysCreate.class) SysMenu sysMenu) {
        this.sysMenuService.create(sysMenu);
        return ServerResponseEntity.ok();
    }

    @SysLog("修改菜单")
    @SysPermissions
    @PutMapping("/update")
    public ServerResponseEntity<?> update(@RequestBody @Validated(SysUpdate.class) SysMenu sysMenu) {
        this.sysMenuService.update(sysMenu);
        return ServerResponseEntity.ok();
    }

    @SysLog("删除菜单")
    @SysPermissions
    @DeleteMapping("/delete")
    public ServerResponseEntity<?> delete(@RequestBody Long id) {
        this.sysMenuService.delete(id);
        return ServerResponseEntity.ok();
    }
}
