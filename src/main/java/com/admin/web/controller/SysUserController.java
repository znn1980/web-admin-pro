package com.admin.web.controller;

import com.admin.web.annotation.*;
import com.admin.web.model.SysSession;
import com.admin.web.model.request.UserLoginRequest;
import com.admin.web.model.request.PageRequest;
import com.admin.web.model.request.UserPassRequest;
import com.admin.web.model.response.ServerResponse;
import com.admin.web.model.entity.SysUser;
import com.admin.web.service.SysUserService;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author znn
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {
    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @SysLog("用户登录")
    @PostMapping("/login.json")
    public ServerResponse<SysSession> login(@RequestBody @Validated UserLoginRequest request) {
        SysUser sysUser = this.sysUserService.login(request, super.getSysCode());
        super.setSysUser(sysUser);
        super.setSysCode(null);
        return ServerResponse.ok(new SysSession(super.getSessionId()));
    }

    @GetMapping("/logout.json")
    public ServerResponse<Void> logout() {
        super.setSysUser(null);
        super.setSysCode(null);
        return ServerResponse.ok();
    }

    @SysPermissions(SysLogin.class)
    @PostMapping("/unlock.json")
    public ServerResponse<Void> unlock(@RequestBody SysUser sysUser) {
        this.sysUserService.unlock(sysUser);
        return ServerResponse.ok();
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/me.json")
    public ServerResponse<SysUser> me() {
        SysUser sysUser = this.sysUserService.show(super.getSysUser().getId());
        return ServerResponse.ok(sysUser);
    }

    @SysPermissions
    @PostMapping("/all.json")
    public ServerResponse<List<SysUser>> all(@RequestBody @Validated PageRequest request) {
        Page<SysUser> sysUsers = this.sysUserService.all(request);
        return ServerResponse.ok(sysUsers.getTotalElements(), sysUsers.getContent());
    }

    @SysLog("创建用户")
    @SysPermissions
    @PostMapping("/create.json")
    public ServerResponse<SysUser> create(@RequestBody @Validated(SysCreate.class) SysUser sysUser) {
        this.sysUserService.create(sysUser);
        return ServerResponse.ok();
    }

    @SysLog("修改用户")
    @SysPermissions
    @PutMapping("/update.json")
    public ServerResponse<SysUser> update(@RequestBody @Validated(SysUpdate.class) SysUser sysUser) {
        return ServerResponse.ok(this.sysUserService.update(sysUser));
    }

    @SysLog("删除用户")
    @SysPermissions
    @DeleteMapping("/delete.json")
    public ServerResponse<Void> delete(@RequestBody Long id) {
        this.sysUserService.delete(id);
        return ServerResponse.ok();
    }

    @SysLog("修改密码")
    @SysPermissions
    @PutMapping("/pass.json")
    public ServerResponse<Void> pass(@RequestBody @Validated UserPassRequest request) {
        this.sysUserService.pass(request);
        return ServerResponse.ok();
    }

    @SysLog("重置密码")
    @SysPermissions
    @PutMapping("/reset.json")
    public ServerResponse<Void> reset(@RequestBody Long id) {
        this.sysUserService.reset(id);
        return ServerResponse.ok();
    }

}
