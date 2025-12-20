package com.admin.web.controller;

import com.admin.web.model.WebServerException;
import com.admin.web.annotation.*;
import com.admin.web.model.ServerResponseEntity;
import com.admin.web.model.SysUser;
import com.admin.web.model.vo.*;
import com.admin.web.service.SysUserService;
import com.admin.web.utils.BeanUtils;
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
        if (!super.isSuperAdmin(sysUser) && sysUser.isDisable()) {
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

    @SysPermissions(SysLogin.class)
    @PostMapping("/unlock")
    public ServerResponseEntity<?> unlock(@RequestBody SysUser sysUser) {
        if (!Objects.equals(super.getSysUser().getPassword(), super.hexPassword(sysUser.getPassword()))) {
            return ServerResponseEntity.fail("密码输入不正确！");
        }
        return ServerResponseEntity.ok();
    }

    @SysPermissions(SysLogin.class)
    @GetMapping("/me")
    public ServerResponseEntity<SysUser> me() {
        return ServerResponseEntity.ok(this.sysUserService.findById(super.getSysUser().getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("用户不存在！"))));
    }

    @SysPermissions
    @PostMapping("/all")
    public ServerResponseEntity<List<SysUser>> all(@RequestBody @Validated PageVo pageVo) {
        Page<SysUser> sysUserLogs = this.sysUserService.findAll(PageRequest.of(pageVo.getPage(), pageVo.getLimit()));
        return ServerResponseEntity.ok(sysUserLogs.getTotalElements(), sysUserLogs.getContent());
    }

    @SysLog("添加用户")
    @SysPermissions
    @PostMapping
    public ServerResponseEntity<SysUser> save(@RequestBody @Validated(SysCreate.class) SysUser sysUser) {
        if (Objects.nonNull(this.sysUserService.findByUsername(sysUser.getUsername()))) {
            return ServerResponseEntity.fail("用户名称已存在！");
        }
        if (Objects.nonNull(this.sysUserService.findByMobile(sysUser.getMobile()))) {
            return ServerResponseEntity.fail("手机号码已存在！");
        }
        if (Objects.nonNull(this.sysUserService.findByEmail(sysUser.getEmail()))) {
            return ServerResponseEntity.fail("邮箱地址已存在！");
        }
        if (!super.isSysAdmin() && sysUser.isSysAdmin()) {
            return ServerResponseEntity.fail("您不是管理员，不能添加管理员用户！");
        }
        sysUser.setPassword(super.hexPassword(sysUser));
        this.sysUserService.save(sysUser);
        return ServerResponseEntity.ok();
    }

    @SysLog("修改用户")
    @SysPermissions
    @PutMapping
    public ServerResponseEntity<SysUser> edit(@RequestBody @Validated(SysUpdate.class) SysUser sysUser) {
        SysUser oldSysUser = this.sysUserService.findById(sysUser.getId())
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("用户不存在！")));
        if (!super.isSuperAdmin() && super.isSuperAdmin(oldSysUser)) {
            return ServerResponseEntity.fail("您不是超级管理员，不能修改超级管理员的信息！");
        }
        if (!super.isSysAdmin() && super.isSysAdmin(oldSysUser)) {
            return ServerResponseEntity.fail("您不是管理员，不能修改管理员的信息！");
        }
        if (!super.isSysAdmin() && !Objects.equals(oldSysUser.isSysAdmin(), sysUser.isSysAdmin())) {
            return ServerResponseEntity.fail("您不是管理员，不能修改管理员状态！");
        }
        if (!Objects.equals(oldSysUser.isSysAdmin(), sysUser.isSysAdmin())
                && Objects.equals(super.getSysUser().getId(), sysUser.getId())) {
            return ServerResponseEntity.fail("您不能修改自己的管理员状态！");
        }
        if (!Objects.equals(oldSysUser.isDisable(), sysUser.isDisable())
                && Objects.equals(super.getSysUser().getId(), sysUser.getId())) {
            return ServerResponseEntity.fail("您不能修改自己的状态！");
        }
        if (!Objects.equals(oldSysUser.getUsername(), sysUser.getUsername())) {
            return ServerResponseEntity.fail("用户名称不能修改！");
        }
        if (!Objects.equals(oldSysUser.getMobile(), sysUser.getMobile())
                && Objects.nonNull(this.sysUserService.findByMobile(sysUser.getMobile()))) {
            return ServerResponseEntity.fail("手机号码已存在！");
        }
        if (!Objects.equals(oldSysUser.getEmail(), sysUser.getEmail())
                && Objects.nonNull(this.sysUserService.findByEmail(sysUser.getEmail()))) {
            return ServerResponseEntity.fail("邮箱地址已存在！");
        }
        BeanUtils.copyNonNullProperties(sysUser, oldSysUser);
        return ServerResponseEntity.ok(this.sysUserService.save(oldSysUser));
    }

    @SysLog("删除用户")
    @SysPermissions
    @DeleteMapping
    public ServerResponseEntity<?> delete(@RequestBody Long id) {
        SysUser sysUser = this.sysUserService.findById(id)
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("用户不存在！")));
        if (Objects.equals(super.getSysUser().getId(), sysUser.getId())) {
            return ServerResponseEntity.fail("不能删除自己！");
        }
        if (super.isSuperAdmin(sysUser)) {
            return ServerResponseEntity.fail("超级管理员不能删除！");
        }
        this.sysUserService.deleteById(id);
        return ServerResponseEntity.ok();
    }

    @SysLog("修改密码")
    @SysPermissions
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
        oldSysUser.setPassTimestamp(LocalDateTime.now());
        oldSysUser.setPassword(userPassVo.getNewPassword());
        this.sysUserService.save(oldSysUser);
        return ServerResponseEntity.ok();
    }

    @SysLog("重置密码")
    @SysPermissions
    @PutMapping("/reset")
    public ServerResponseEntity<?> reset(@RequestBody Long id) {
        SysUser sysUser = this.sysUserService.findById(id)
                .orElseThrow(() -> new WebServerException(ServerResponseEntity.fail("用户不存在！")));
        if (Objects.equals(super.getSysUser().getId(), sysUser.getId())) {
            return ServerResponseEntity.fail("不能重置自己的密码！");
        }
        if (super.isSuperAdmin(sysUser)) {
            return ServerResponseEntity.fail("超级管理员的密码不能重置！");
        }
        sysUser.setPassword(super.hexPassword(sysUser));
        this.sysUserService.save(sysUser);
        return ServerResponseEntity.ok();
    }

}
