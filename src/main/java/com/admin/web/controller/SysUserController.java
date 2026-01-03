package com.admin.web.controller;

import com.admin.web.annotation.*;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysUser;
import com.admin.web.model.vo.*;
import com.admin.web.service.SysNoticeService;
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
    private final SysNoticeService sysNoticeService;

    public SysUserController(SysUserService sysUserService, SysNoticeService sysNoticeService) {
        this.sysUserService = sysUserService;
        this.sysNoticeService = sysNoticeService;
    }

    @SysLog("用户登录")
    @PostMapping("/login")
    public ServerResponseEntity<?> login(@RequestBody @Validated UserLoginVo vo) {
        SysUser sysUser = this.sysUserService.login(vo, super.getSysCode());
        super.setSysUser(sysUser);
        super.setSysCode(null);
        return ServerResponseEntity.ok(new SessionVo(super.getSessionId()));
    }

    @SysLog("用户退出")
    @GetMapping("/logout")
    public ServerResponseEntity<?> logout() {
        super.setSysUser(null);
        super.setSysCode(null);
        return ServerResponseEntity.ok();
    }

    @SysPermissions(SysLogin.class)
    @PostMapping("/unlock")
    public ServerResponseEntity<?> unlock(@RequestBody SysUser sysUser) {
        this.sysUserService.unlock(sysUser);
        return ServerResponseEntity.ok();
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/me")
    public ServerResponseEntity<SysUser> me() {
        SysUser sysUser = this.sysUserService.show(super.getSysUser().getId());
        Long unRead = this.sysNoticeService.countByUserId(super.getSysUser().getId());
        return ServerResponseEntity.ok(unRead, sysUser);
    }

    @SysPermissions
    @PostMapping("/all")
    public ServerResponseEntity<List<SysUser>> all(@RequestBody @Validated PageVo vo) {
        Page<SysUser> sysUsers = this.sysUserService.all(vo);
        return ServerResponseEntity.ok(sysUsers.getTotalElements(), sysUsers.getContent());
    }

    @SysLog("创建用户")
    @SysPermissions
    @PostMapping("/create")
    public ServerResponseEntity<SysUser> create(@RequestBody @Validated(SysCreate.class) SysUser sysUser) {
        this.sysUserService.create(sysUser);
        return ServerResponseEntity.ok();
    }

    @SysLog("修改用户")
    @SysPermissions
    @PutMapping("/update")
    public ServerResponseEntity<SysUser> update(@RequestBody @Validated(SysUpdate.class) SysUser sysUser) {
        return ServerResponseEntity.ok(this.sysUserService.update(sysUser));
    }

    @SysLog("删除用户")
    @SysPermissions
    @DeleteMapping("/delete")
    public ServerResponseEntity<?> delete(@RequestBody Long id) {
        this.sysUserService.delete(id);
        return ServerResponseEntity.ok();
    }

    @SysLog("修改密码")
    @SysPermissions
    @PutMapping("/pass")
    public ServerResponseEntity<?> pass(@RequestBody @Validated UserPassVo vo) {
        this.sysUserService.pass(vo);
        return ServerResponseEntity.ok();
    }

    @SysLog("重置密码")
    @SysPermissions
    @PutMapping("/reset")
    public ServerResponseEntity<?> reset(@RequestBody Long id) {
        this.sysUserService.reset(id);
        return ServerResponseEntity.ok();
    }

}
