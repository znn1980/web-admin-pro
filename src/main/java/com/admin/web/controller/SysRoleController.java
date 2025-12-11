package com.admin.web.controller;

import com.admin.web.WebServerException;
import com.admin.web.annotation.SysLog;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysRole;
import com.admin.web.service.SysRoleService;
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
    @GetMapping("/page")
    public ServerResponseEntity<List<SysRole>> page() {
        return ServerResponseEntity.ok(this.sysRoleService.findByAll());
    }

    @SysLog("添加角色")
    @SysPermissions
    @PostMapping()
    public ServerResponseEntity<?> save(@RequestBody @Validated SysRole sysRole) {
        if (Objects.nonNull(this.sysRoleService.findByName(sysRole.getName()))) {
            return ServerResponseEntity.fail("角色名称已存在！");
        }
        if (Objects.isNull(this.sysRoleService.save(sysRole))) {
            return ServerResponseEntity.fail("角色添加失败！");
        }
        return ServerResponseEntity.ok();
    }

    @SysLog("修改角色")
    @SysPermissions
    @PutMapping()
    public ServerResponseEntity<?> edit(@RequestBody @Validated SysRole sysRole) {
        SysRole oldSysRole = this.sysRoleService.findById(sysRole.getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("角色不存在！")));
        if (!Objects.equals(oldSysRole.getName(), sysRole.getName())
                && Objects.nonNull(this.sysRoleService.findByName(sysRole.getName()))) {
            return ServerResponseEntity.fail("角色名称已存在！");
        }
        oldSysRole.setName(sysRole.getName());
        oldSysRole.setRemark(sysRole.getRemark());
        oldSysRole.setDisable(sysRole.isDisable());
        if (Objects.isNull(this.sysRoleService.save(oldSysRole))) {
            return ServerResponseEntity.fail("角色修改失败！");
        }
        return ServerResponseEntity.ok();
    }

    @SysLog("删除角色")
    @SysPermissions
    @DeleteMapping()
    public ServerResponseEntity<?> delete(@RequestBody Long id) {
        this.sysRoleService.deleteById(id);
        return ServerResponseEntity.ok();
    }

}
