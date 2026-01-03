package com.admin.web.controller;

import com.admin.web.annotation.SysCreate;
import com.admin.web.annotation.SysLog;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.annotation.SysUpdate;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysRole;
import com.admin.web.model.vo.MoveVo;
import com.admin.web.service.SysRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author znn
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {
    private final SysRoleService sysRoleService;

    public SysRoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @SysPermissions
    @GetMapping("/all")
    public ServerResponseEntity<List<SysRole>> all() {
        List<SysRole> sysRoles = this.sysRoleService.all();
        return ServerResponseEntity.ok(sysRoles);
    }

    @SysLog("移动角色")
    @SysPermissions
    @PutMapping("/move")
    public ServerResponseEntity<?> move(@RequestBody MoveVo vo) {
        this.sysRoleService.move(vo);
        return ServerResponseEntity.ok();
    }

    @SysLog("创建角色")
    @SysPermissions
    @PostMapping("/create")
    public ServerResponseEntity<?> create(@RequestBody @Validated(SysCreate.class) SysRole sysRole) {
        this.sysRoleService.create(sysRole);
        return ServerResponseEntity.ok();
    }

    @SysLog("修改角色")
    @SysPermissions
    @PutMapping("/update")
    public ServerResponseEntity<?> update(@RequestBody @Validated(SysUpdate.class) SysRole sysRole) {
        this.sysRoleService.update(sysRole);
        return ServerResponseEntity.ok();
    }

    @SysLog("删除角色")
    @SysPermissions
    @DeleteMapping("/delete")
    public ServerResponseEntity<?> delete(@RequestBody Long id) {
        this.sysRoleService.delete(id);
        return ServerResponseEntity.ok();
    }

}
