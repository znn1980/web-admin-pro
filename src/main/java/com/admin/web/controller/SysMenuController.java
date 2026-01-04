package com.admin.web.controller;

import com.admin.web.annotation.*;
import com.admin.web.model.ServerResponse;
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
    @GetMapping("/me.json")
    public ServerResponse<List<SysMenu>> me() {
        List<SysMenu> sysMenus = this.sysMenuService.all(super.getSysUser());
        return ServerResponse.ok(sysMenus);
    }

    @SysPermissions
    @GetMapping("/all.json")
    public ServerResponse<List<SysMenu>> all() {
        List<SysMenu> sysMenus = this.sysMenuService.all();
        return ServerResponse.ok(sysMenus);
    }

    @SysLog("移动菜单")
    @SysPermissions
    @PutMapping("/move.json")
    public ServerResponse<?> move(@RequestBody MoveVo vo) {
        this.sysMenuService.move(vo);
        return ServerResponse.ok();
    }

    @SysLog("创建菜单")
    @SysPermissions
    @PostMapping("/create.json")
    public ServerResponse<?> create(@RequestBody @Validated(SysCreate.class) SysMenu sysMenu) {
        this.sysMenuService.create(sysMenu);
        return ServerResponse.ok();
    }

    @SysLog("修改菜单")
    @SysPermissions
    @PutMapping("/update.json")
    public ServerResponse<?> update(@RequestBody @Validated(SysUpdate.class) SysMenu sysMenu) {
        this.sysMenuService.update(sysMenu);
        return ServerResponse.ok();
    }

    @SysLog("删除菜单")
    @SysPermissions
    @DeleteMapping("/delete.json")
    public ServerResponse<?> delete(@RequestBody Long id) {
        this.sysMenuService.delete(id);
        return ServerResponse.ok();
    }
}
