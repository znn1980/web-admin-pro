package com.admin.web.dao;

import com.admin.web.model.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author znn
 */
public interface SysUserDao extends JpaRepository<SysUser, Long> {
    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    SysUser findByUsername(String username);

    /**
     * 通过手机号查询用户
     *
     * @param mobile 手机号
     * @return 用户
     */
    SysUser findByMobile(String mobile);

    /**
     * 通过邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户
     */
    SysUser findByEmail(String email);
}
