package com.admin.web.dao;

import com.admin.web.model.SysMenu;
import com.admin.web.model.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author znn
 */
public interface SysUserDao extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysMenu> {
    /**
     * 查询用户表
     *
     * @param username 用户名
     * @return 用户
     */
    SysUser findByUsername(String username);

    /**
     * 查询用户表
     *
     * @param phone 手机号
     * @return 用户
     */
    SysUser findByPhone(String phone);

    /**
     * 查询用户表
     *
     * @param email 邮箱
     * @return 用户
     */
    SysUser findByEmail(String email);
}
