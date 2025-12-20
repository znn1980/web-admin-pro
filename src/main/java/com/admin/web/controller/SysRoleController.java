package com.admin.web.controller;

import com.admin.web.model.WebServerException;
import com.admin.web.annotation.SysCreate;
import com.admin.web.annotation.SysLog;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.annotation.SysUpdate;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysRole;
import com.admin.web.model.vo.MoveVo;
import com.admin.web.service.SysRoleService;
import com.admin.web.utils.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
        return ServerResponseEntity.ok(this.sysRoleService.findAll());
    }

    @SysLog("移动角色")
    @SysPermissions
    @PutMapping("/move")
    public ServerResponseEntity<?> move(@RequestBody MoveVo moveVo) {
        SysRole sysRole = this.sysRoleService.findById(moveVo.getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("角色不存在！")));
        this.sysRoleService.move(sysRole, moveVo.getMove());
        return ServerResponseEntity.ok();
    }

    @SysLog("添加角色")
    @SysPermissions
    @PostMapping
    public ServerResponseEntity<?> save(@RequestBody @Validated(SysCreate.class) SysRole sysRole) {
        if (Objects.nonNull(this.sysRoleService.findByName(sysRole.getName()))) {
            return ServerResponseEntity.fail("角色名称已存在！");
        }
        this.sysRoleService.save(sysRole);
        return ServerResponseEntity.ok();
    }

    @SysLog("修改角色")
    @SysPermissions
    @PutMapping
    public ServerResponseEntity<?> edit(@RequestBody @Validated(SysUpdate.class) SysRole sysRole) {
        SysRole oldSysRole = this.sysRoleService.findById(sysRole.getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("角色不存在！")));
        if (!Objects.equals(oldSysRole.getName(), sysRole.getName())
                && Objects.nonNull(this.sysRoleService.findByName(sysRole.getName()))) {
            return ServerResponseEntity.fail("角色名称已存在！");
        }
        BeanUtils.copyNonNullProperties(sysRole, oldSysRole);
        this.sysRoleService.save(oldSysRole);
        return ServerResponseEntity.ok();
    }

    @SysLog("删除角色")
    @SysPermissions
    @DeleteMapping
    public ServerResponseEntity<?> delete(@RequestBody Long id) {
        if (this.sysRoleService.existsByRoleId(id)) {
            return ServerResponseEntity.fail("此角色已绑定用户，请先解绑用户下的角色！");
        }
        this.sysRoleService.deleteById(id);
        return ServerResponseEntity.ok();
    }

}
