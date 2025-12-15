package com.admin.web.controller;

import com.admin.web.WebServerException;
import com.admin.web.annotation.*;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysUser;
import com.admin.web.model.vo.*;
import com.admin.web.service.SysUserService;
import com.admin.web.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
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

    @SysPermissions
    @PostMapping("/page")
    public ServerResponseEntity<List<SysUser>> page(@RequestBody @Validated PageVo pageVo) {
        Page<SysUser> sysUserLogs = this.sysUserService.findAll(PageRequest.of(pageVo.getPage(), pageVo.getLimit()));
        return ServerResponseEntity.ok(sysUserLogs.getTotalElements(), sysUserLogs.getContent());
    }

    @SysLog("修改用户")
    @SysPermissions()
    @PutMapping()
    public ServerResponseEntity<SysUser> edit(@RequestBody @Validated(SysUpdate.class) SysUser sysUser) {
        SysUser oldSysUser = this.sysUserService.findById(sysUser.getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("用户不存在！")));
        if (!Objects.equals(oldSysUser.getMobile(), sysUser.getMobile())
                && Objects.nonNull(this.sysUserService.findByMobile(sysUser.getMobile()))) {
            return ServerResponseEntity.fail("手机号码已存在！");
        }
        if (!Objects.equals(oldSysUser.getEmail(), sysUser.getEmail())
                && Objects.nonNull(this.sysUserService.findByEmail(sysUser.getEmail()))) {
            return ServerResponseEntity.fail("邮箱地址已存在！");
        }
        return ServerResponseEntity.ok(this.sysUserService.save(sysUser));
    }

    @SysLog("修改用户")
    @SysPermissions()
    @PutMapping("/me")
    public ServerResponseEntity<SysUser> me(@RequestBody @Validated SysUser sysUser) {
        SysUser oldSysUser = this.sysUserService.findById(super.getSysUser().getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("用户不存在！")));
        oldSysUser.setAvatar(sysUser.getAvatar());
        oldSysUser.setRemark(sysUser.getRemark());
        if (!Objects.equals(oldSysUser.getMobile(), sysUser.getMobile())
                && Objects.nonNull(this.sysUserService.findByMobile(sysUser.getMobile()))) {
            return ServerResponseEntity.fail("手机号码已存在！");
        }
        oldSysUser.setMobile(sysUser.getMobile());
        if (!Objects.equals(oldSysUser.getEmail(), sysUser.getEmail())
                && Objects.nonNull(this.sysUserService.findByEmail(sysUser.getEmail()))) {
            return ServerResponseEntity.fail("邮箱地址已存在！");
        }
        oldSysUser.setEmail(sysUser.getEmail());
        return ServerResponseEntity.ok(this.sysUserService.save(oldSysUser));
    }

    @SysLog("修改密码")
    @SysPermissions()
    @PutMapping("/pass")
    public ServerResponseEntity<?> pass(@RequestBody @Validated UserPassVo userPassVo) {
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


}
