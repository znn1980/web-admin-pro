package com.admin.web.service;

import com.admin.web.annotation.SysLogin;
import com.admin.web.annotation.SysPermissions;
import com.admin.web.repository.SysUserRepository;
import com.admin.web.exception.ServerResponseException;
import com.admin.web.model.SysMenu;
import com.admin.web.model.SysRole;
import com.admin.web.model.SysUser;
import com.admin.web.model.enums.ResponseCode;
import com.admin.web.model.vo.PageVo;
import com.admin.web.model.vo.UserLoginVo;
import com.admin.web.model.vo.UserPassVo;
import com.admin.web.utils.BeanUtils;
import com.admin.web.utils.SecurityUtils;
import com.admin.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author znn
 */
@Service
public class SysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserService.class);
    private final SysUserRepository sysUserRepository;

    public SysUserService(SysUserRepository sysUserRepository) {
        this.sysUserRepository = sysUserRepository;
    }

    public SysUser login(UserLoginVo vo, String sysCode) {
        if (!Objects.equals(vo.sysCode(), sysCode)) {
            throw new ServerResponseException("验证码输入不正确！");
        }
        SysUser sysUser = Optional.ofNullable(this.sysUserRepository.findByUsername(vo.username()))
                .orElseGet(() -> Optional.ofNullable(this.sysUserRepository.findByPhone(vo.username()))
                        .orElseGet(() -> this.sysUserRepository.findByEmail(vo.username())));
        if (Objects.isNull(sysUser) || !Objects.equals(sysUser.getPassword()
                , SecurityUtils.hexPassword(vo.password()))) {
            throw new ServerResponseException("登录失败，请检查用户名密码是否正确！");
        }
        if (!SecurityUtils.isSuperAdmin(sysUser) && sysUser.isDisable()) {
            throw new ServerResponseException("账号未启用，请联系管理员！");
        }
        return sysUser;
    }

    public void unlock(SysUser sysUser) {
        if (!Objects.equals(this.getSysUser().getPassword()
                , SecurityUtils.hexPassword(sysUser.getPassword()))) {
            throw new ServerResponseException("密码输入不正确！");
        }
    }

    public SysUser show(Long id) {
        return this.sysUserRepository.findById(id)
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
    }

    public Page<SysUser> all(PageVo vo) {
        return this.sysUserRepository.findAll(PageVo.of(vo));
    }

    public void create(SysUser sysUser) {
        if (!SecurityUtils.isSuperAdmin(this.getSysUser()) && sysUser.isSysAdmin()) {
            throw new ServerResponseException("您不是超级管理员，不能创建管理员用户！");
        }
        if (Objects.nonNull(this.sysUserRepository.findByUsername(sysUser.getUsername()))) {
            throw new ServerResponseException("用户名称已存在！");
        }
        if (Objects.nonNull(this.sysUserRepository.findByPhone(sysUser.getPhone()))) {
            throw new ServerResponseException("手机号码已存在！");
        }
        if (Objects.nonNull(this.sysUserRepository.findByEmail(sysUser.getEmail()))) {
            throw new ServerResponseException("邮箱地址已存在！");
        }
        sysUser.setPassword(SecurityUtils.hexPassword(sysUser));
        this.sysUserRepository.save(sysUser);
    }

    public SysUser update(SysUser sysUser) {
        SysUser oldSysUser = this.sysUserRepository.findById(sysUser.getId())
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (!SecurityUtils.isSuperAdmin(this.getSysUser()) && SecurityUtils.isSuperAdmin(oldSysUser)) {
            throw new ServerResponseException("您不是超级管理员，不能修改超级管理员的信息！");
        }
        if (!SecurityUtils.isSuperAdmin(this.getSysUser()) && !Objects.equals(oldSysUser.isSysAdmin(), sysUser.isSysAdmin())) {
            throw new ServerResponseException("您不是超级管理员，不能修改管理员状态！");
        }
        if (!SecurityUtils.isSysAdmin(this.getSysUser()) && SecurityUtils.isSysAdmin(oldSysUser)) {
            throw new ServerResponseException("您不是管理员，不能修改管理员的信息！");
        }
        if (!Objects.equals(oldSysUser.isSysAdmin(), sysUser.isSysAdmin())
                && Objects.equals(this.getSysUser().getId(), sysUser.getId())) {
            throw new ServerResponseException("您不能修改自己的管理员状态！");
        }
        if (!Objects.equals(oldSysUser.isDisable(), sysUser.isDisable())
                && Objects.equals(this.getSysUser().getId(), sysUser.getId())) {
            throw new ServerResponseException("您不能修改自己的状态！");
        }
        if (!Objects.equals(oldSysUser.getUsername(), sysUser.getUsername())) {
            throw new ServerResponseException("用户名称不能修改！");
        }
        if (!Objects.equals(oldSysUser.getPhone(), sysUser.getPhone())
                && Objects.nonNull(this.sysUserRepository.findByPhone(sysUser.getPhone()))) {
            throw new ServerResponseException("手机号码已存在！");
        }
        if (!Objects.equals(oldSysUser.getEmail(), sysUser.getEmail())
                && Objects.nonNull(this.sysUserRepository.findByEmail(sysUser.getEmail()))) {
            throw new ServerResponseException("邮箱地址已存在！");
        }
        oldSysUser.setNotices(null);
        BeanUtils.copyNonNullProperties(sysUser, oldSysUser);
        return this.sysUserRepository.save(oldSysUser);
    }

    public void delete(Long id) {
        SysUser sysUser = this.sysUserRepository.findById(id)
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (Objects.equals(this.getSysUser().getId(), sysUser.getId())) {
            throw new ServerResponseException("不能删除自己！");
        }
        if (SecurityUtils.isSuperAdmin(sysUser)) {
            throw new ServerResponseException("超级管理员不能删除！");
        }
        this.sysUserRepository.deleteById(id);
    }

    public void pass(UserPassVo vo) {
        if (!Objects.equals(vo.newPassword(), vo.confirmPassword())) {
            throw new ServerResponseException("新密码与确认密码输入不一致！");
        }
        if (Objects.equals(vo.newPassword(), vo.oldPassword())) {
            throw new ServerResponseException("新密码不能与原密码重复！");
        }
        SysUser sysUser = this.sysUserRepository.findById(this.getSysUser().getId())
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (!Objects.equals(sysUser.getPassword(), SecurityUtils.hexPassword(vo.oldPassword()))) {
            throw new ServerResponseException("原密码输入不正确！");
        }
        sysUser.setPassTimestamp(LocalDateTime.now());
        sysUser.setPassword(SecurityUtils.hexPassword(vo.newPassword()));
        this.sysUserRepository.save(sysUser);
    }

    public void reset(Long id) {
        SysUser sysUser = this.sysUserRepository.findById(id)
                .orElseThrow(() -> new ServerResponseException(ResponseCode.NOT_FOUND));
        if (Objects.equals(this.getSysUser().getId(), sysUser.getId())) {
            throw new ServerResponseException("不能重置自己的密码！");
        }
        if (SecurityUtils.isSuperAdmin(sysUser)) {
            throw new ServerResponseException("超级管理员的密码不能重置！");
        }
        sysUser.setPassword(SecurityUtils.hexPassword(sysUser));
        this.sysUserRepository.save(sysUser);
    }

    public void hasPermissions(SysPermissions sysPermissions) {
        SysUser sysUser = Optional.ofNullable(this.getSysUser())
                .orElseThrow(() -> new ServerResponseException(ResponseCode.LOGOUT));
        if (!SecurityUtils.isSuperAdmin(sysUser)
                && !Arrays.asList(sysPermissions.value()).contains(SysLogin.class)) {
            if (!this.hasPermissions(this.sysUserRepository.findById(sysUser.getId()).orElseThrow(() ->
                    new ServerResponseException(ResponseCode.NOT_FOUND)).getRoles())) {
                throw new ServerResponseException(ResponseCode.DENIED);
            }
        }
    }

    private boolean hasPermissions(Set<SysRole> sysRoles) {
        PathMatcher matcher = new AntPathMatcher();
        for (SysRole sysRole : sysRoles) {
            for (SysMenu sysMenu : sysRole.getMenus()) {
                log.info("SYS-PERMISSIONS => {}:{} => {}:{}", sysMenu.getMethod(), sysMenu.getUrl()
                        , WebUtils.getRequest().getMethod(), WebUtils.getRequest().getRequestURI());
                if (!sysMenu.isDisable() && StringUtils.hasText(sysMenu.getUrl())
                        && matcher.match(sysMenu.getUrl(), WebUtils.getRequest().getRequestURI())
                        && Objects.equals(sysMenu.getMethod(), WebUtils.getRequest().getMethod())) {
                    return true;
                }
            }
        }
        return false;
    }

    SysUser getSysUser() {
        return SecurityUtils.getSysUser(WebUtils.getRequest());
    }
}
