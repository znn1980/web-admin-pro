package com.admin.web.controller;

import com.admin.web.WebServerException;
import com.admin.web.annotation.*;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysUser;
import com.admin.web.service.SysUserService;
import com.admin.web.utils.SecurityUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

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
    @PostMapping("/login")
    public ServerResponseEntity<?> login(@RequestBody @Validated(SysLogin.class) SysUser sysUser) {
        if (!Objects.equals(sysUser.getSysCode(), super.getSysCode())) {
            return ServerResponseEntity.fail("验证码输入不正确！");
        }
        SysUser oldSysUser = this.sysUserService.login(sysUser);
        sysUser.setPassword(super.hexPassword(sysUser.getPassword()));
        if (Objects.isNull(oldSysUser) || !Objects.equals(oldSysUser.getPassword(), sysUser.getPassword())) {
            return ServerResponseEntity.fail("登录失败，请检查用户名密码是否正确！");
        }
        if (!Objects.equals(oldSysUser.getUsername(), SecurityUtils.getSysUser().getUsername())
                && oldSysUser.isDisable()) {
            return ServerResponseEntity.fail("账号未启用，请联系管理员！");
        }
        oldSysUser.setSessionId(super.getSessionId());
        oldSysUser.setIp(super.getClientIp());
        super.setSysUser(oldSysUser);
        super.setSysCode(null);
        return ServerResponseEntity.ok();
    }

    @SysLog("用户退出")
    @GetMapping("/logout")
    public ServerResponseEntity<?> logout() {
        super.setSysUser(null);
        super.setSysCode(null);
        return ServerResponseEntity.ok();
    }

    @SysLog("修改用户信息")
    @SysPermissions()
    @PutMapping()
    public ServerResponseEntity<SysUser> edit(@RequestBody @Validated(SysUpdate.class) SysUser sysUser) {
        SysUser oldSysUser = this.sysUserService.findById(super.getSysUser().getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("用户不存在！")));
        if (!Objects.equals(oldSysUser.getMobile(), sysUser.getMobile())
                && Objects.nonNull(this.sysUserService.findByMobile(sysUser.getMobile()))) {
            return ServerResponseEntity.fail("手机号码已存在！");
        }
        if (!Objects.equals(oldSysUser.getEmail(), sysUser.getEmail())
                && Objects.nonNull(this.sysUserService.findByEmail(sysUser.getEmail()))) {
            return ServerResponseEntity.fail("邮箱地址已存在！");
        }
        oldSysUser.setMobile(sysUser.getMobile());
        oldSysUser.setEmail(sysUser.getEmail());
        oldSysUser.setAvatar(sysUser.getAvatar());
        oldSysUser.setRemark(sysUser.getRemark());
        if (Objects.isNull(this.sysUserService.save(oldSysUser))) {
            return ServerResponseEntity.fail("用户修改失败！");
        }
        oldSysUser.setSessionId(super.getSessionId());
        oldSysUser.setIp(super.getClientIp());
        return ServerResponseEntity.ok(oldSysUser);
    }

    @SysLog("修改用户密码")
    @SysPermissions()
    @PutMapping("/password")
    public ServerResponseEntity<?> password(@RequestBody @Validated(SysPassword.class) SysUser sysUser) {
        sysUser.setOldPassword(super.hexPassword(sysUser.getOldPassword()));
        sysUser.setNewPassword(super.hexPassword(sysUser.getNewPassword()));
        sysUser.setConfirmPassword(super.hexPassword(sysUser.getConfirmPassword()));
        if (!Objects.equals(sysUser.getNewPassword(), sysUser.getConfirmPassword())) {
            return ServerResponseEntity.fail("新密码与确认密码输入不一致！");
        }
        if (Objects.equals(sysUser.getNewPassword(), sysUser.getOldPassword())) {
            return ServerResponseEntity.fail("新密码不能与原密码重复！");
        }
        SysUser oldSysUser = this.sysUserService.findById(super.getSysUser().getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("用户不存在！")));
        if (!Objects.equals(oldSysUser.getPassword(), sysUser.getOldPassword())) {
            return ServerResponseEntity.fail("原密码输入不正确！");
        }
        oldSysUser.setPasswordTimestamp(LocalDateTime.now());
        oldSysUser.setPassword(sysUser.getNewPassword());
        if (Objects.isNull(this.sysUserService.save(oldSysUser))) {
            return ServerResponseEntity.fail("密码修改失败！");
        }
        return ServerResponseEntity.ok();
    }

    @SysPermissions()
    @GetMapping()
    public ServerResponseEntity<SysUser> query() {
        SysUser oldSysUser = this.sysUserService.findById(super.getSysUser().getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("用户不存在！")));
        oldSysUser.setSessionId(super.getSessionId());
        oldSysUser.setIp(super.getClientIp());
        return ServerResponseEntity.ok(oldSysUser);
    }

    @SysPermissions()
    @GetMapping("/{id}")
    public ServerResponseEntity<SysUser> query(@PathVariable Long id) {
        return ServerResponseEntity.ok(this.sysUserService.findById(id).orElseThrow(() ->
                new WebServerException(ServerResponseEntity.fail("用户不存在！"))));
    }
}
