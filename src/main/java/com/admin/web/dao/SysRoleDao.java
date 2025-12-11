package com.admin.web.dao;

import com.admin.web.model.SysMenu;
import com.admin.web.model.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author znn
 */
public interface SysRoleDao extends JpaRepository<SysRole, Long>, JpaSpecificationExecutor<SysMenu> {
    /**
     * 通过角色名称查询角色
     *
     * @param name 角色名称
     * @return 角色
     */
    SysRole findByName(String name);
}
