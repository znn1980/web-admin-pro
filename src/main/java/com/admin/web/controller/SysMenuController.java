package com.admin.web.controller;

import com.admin.web.annotation.SysPermissions;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysMenu;
import com.admin.web.service.SysMenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @SysPermissions
    @GetMapping("/page")
    public ServerResponseEntity<List<SysMenu>> page() {
        return ServerResponseEntity.ok(this.sysMenuService.findAll());
    }
}
