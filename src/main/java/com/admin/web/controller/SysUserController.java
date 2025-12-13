package com.admin.web.controller;

import com.admin.web.WebServerException;
import com.admin.web.annotation.*;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysUser;
import com.admin.web.model.vo.SessionVo;
import com.admin.web.model.vo.UserLoginVo;
import com.admin.web.model.vo.UserPassVo;
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
    public ServerResponseEntity<?> login(@RequestBody @Validated UserLoginVo userLoginVo) {
        userLoginVo.setPassword(super.hexPassword(userLoginVo.getPassword()));
        if (!Objects.equals(userLoginVo.getSysCode(), super.getSysCode())) {
            return ServerResponseEntity.fail("验证码输入不正确！");
        }
        SysUser sysUser = this.sysUserService.login(userLoginVo);
        if (Objects.isNull(sysUser) || !Objects.equals(sysUser.getPassword(), userLoginVo.getPassword())) {
            return ServerResponseEntity.fail("登录失败，请检查用户名密码是否正确！");
        }
        if (!Objects.equals(sysUser.getUsername(), SecurityUtils.getSysUser().getUsername())
                && sysUser.isDisable()) {
            return ServerResponseEntity.fail("账号未启用，请联系管理员！");
        }
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

    @SysLog("修改用户")
    @SysPermissions()
    @PutMapping()
    public ServerResponseEntity<SysUser> edit(@RequestBody @Validated(SysUpdate.class) SysUser sysUser) {
        SysUser oldSysUser = this.sysUserService.findById(sysUser.getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("用户不存在！")));
        return ServerResponseEntity.ok(this.save(oldSysUser, sysUser));
    }

    @SysLog("修改用户")
    @SysPermissions()
    @PutMapping("/me")
    public ServerResponseEntity<SysUser> me(@RequestBody @Validated SysUser sysUser) {
        SysUser oldSysUser = this.sysUserService.findById(super.getSysUser().getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("用户不存在！")));
        sysUser.setDisable(oldSysUser.isDisable());
        sysUser.setSysAdmin(oldSysUser.isSysAdmin());
        return ServerResponseEntity.ok(this.save(oldSysUser, sysUser));
    }

    @SysLog("修改密码")
    @SysPermissions()
    @PutMapping("/password")
    public ServerResponseEntity<?> password(@RequestBody @Validated UserPassVo userPassVo) {
        userPassVo.setOldPassword(super.hexPassword(userPassVo.getOldPassword()));
        userPassVo.setNewPassword(super.hexPassword(userPassVo.getNewPassword()));
        userPassVo.setConfirmPassword(super.hexPassword(userPassVo.getConfirmPassword()));
        if (!Objects.equals(userPassVo.getNewPassword(), userPassVo.getConfirmPassword())) {
            return ServerResponseEntity.fail("新密码与确认密码输入不一致！");
        }
        if (Objects.equals(userPassVo.getNewPassword(), userPassVo.getOldPassword())) {
            return ServerResponseEntity.fail("新密码不能与原密码重复！");
        }
        SysUser oldSysUser = this.sysUserService.findById(super.getSysUser().getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("用户不存在！")));
        if (!Objects.equals(oldSysUser.getPassword(), userPassVo.getOldPassword())) {
            return ServerResponseEntity.fail("原密码输入不正确！");
        }
        oldSysUser.setPasswordTimestamp(LocalDateTime.now());
        oldSysUser.setPassword(userPassVo.getNewPassword());
        this.sysUserService.save(oldSysUser);
        return ServerResponseEntity.ok();
    }

    @SysPermissions()
    @GetMapping()
    public ServerResponseEntity<SysUser> query() {
        return ServerResponseEntity.ok(this.sysUserService.findById(super.getSysUser().getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("用户不存在！"))));
    }

    public SysUser save(SysUser oldSysUser, SysUser sysUser) {
        if (!Objects.equals(oldSysUser.getMobile(), sysUser.getMobile())
                && Objects.nonNull(this.sysUserService.findByMobile(sysUser.getMobile()))) {
            throw new WebServerException(ServerResponseEntity.fail("手机号码已存在！"));
        }
        if (!Objects.equals(oldSysUser.getEmail(), sysUser.getEmail())
                && Objects.nonNull(this.sysUserService.findByEmail(sysUser.getEmail()))) {
            throw new WebServerException(ServerResponseEntity.fail("邮箱地址已存在！"));
        }
        oldSysUser.setMobile(sysUser.getMobile());
        oldSysUser.setEmail(sysUser.getEmail());
        oldSysUser.setAvatar(sysUser.getAvatar());
        oldSysUser.setRemark(sysUser.getRemark());
        oldSysUser.setDisable(sysUser.isDisable());
        oldSysUser.setSysAdmin(sysUser.isSysAdmin());
        return this.sysUserService.save(oldSysUser);
    }

}
