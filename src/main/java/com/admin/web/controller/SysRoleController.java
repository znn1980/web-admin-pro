package com.admin.web.controller;

import com.admin.web.annotation.SysCreate;
import com.admin.web.annotation.SysLog;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.annotation.SysUpdate;
import com.admin.web.model.ServerResponse;
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
    @GetMapping("/all.json")
    public ServerResponse<List<SysRole>> all() {
        List<SysRole> sysRoles = this.sysRoleService.all();
        return ServerResponse.ok(sysRoles);
    }

    @SysLog("移动角色")
    @SysPermissions
    @PutMapping("/move.json")
    public ServerResponse<Void> move(@RequestBody MoveVo vo) {
        this.sysRoleService.move(vo);
        return ServerResponse.ok();
    }

    @SysLog("创建角色")
    @SysPermissions
    @PostMapping("/create.json")
    public ServerResponse<Void> create(@RequestBody @Validated(SysCreate.class) SysRole sysRole) {
        this.sysRoleService.create(sysRole);
        return ServerResponse.ok();
    }

    @SysLog("修改角色")
    @SysPermissions
    @PutMapping("/update.json")
    public ServerResponse<Void> update(@RequestBody @Validated(SysUpdate.class) SysRole sysRole) {
        this.sysRoleService.update(sysRole);
        return ServerResponse.ok();
    }

    @SysLog("删除角色")
    @SysPermissions
    @DeleteMapping("/delete.json")
    public ServerResponse<Void> delete(@RequestBody Long id) {
        this.sysRoleService.delete(id);
        return ServerResponse.ok();
    }

}
