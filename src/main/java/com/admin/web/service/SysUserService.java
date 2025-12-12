package com.admin.web.service;

import com.admin.web.dao.SysUserDao;
import com.admin.web.model.SysUser;
import com.admin.web.model.vo.UserLoginVo;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author znn
 */
@Service
public class SysUserService {
    private final SysUserDao sysUserDao;

    public SysUserService(SysUserDao sysUserDao) {
        this.sysUserDao = sysUserDao;
    }

    public SysUser login(UserLoginVo userLoginVo) {
        return Optional.ofNullable(this.findByUsername(userLoginVo.getUsername()))
                .orElseGet(() -> Optional.ofNullable(this.findByMobile(userLoginVo.getUsername()))
                        .orElseGet(() -> this.findByEmail(userLoginVo.getUsername())));
    }

    public SysUser save(SysUser sysUser) {
        return this.sysUserDao.save(sysUser);
    }

    public SysUser findByUsername(String username) {
        return this.sysUserDao.findByUsername(username);
    }

    public SysUser findByMobile(String mobile) {
        return this.sysUserDao.findByMobile(mobile);
    }

    public SysUser findByEmail(String email) {
        return this.sysUserDao.findByEmail(email);
    }

    public Optional<SysUser> findById(Long id) {
        return this.sysUserDao.findById(id);
    }
}
