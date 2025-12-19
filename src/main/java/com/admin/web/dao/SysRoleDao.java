package com.admin.web.dao;

import com.admin.web.model.SysMenu;
import com.admin.web.model.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author znn
 */
public interface SysRoleDao extends JpaRepository<SysRole, Long>, JpaSpecificationExecutor<SysMenu> {
    /**
     * 查询角色表
     *
     * @param name 角色名称
     * @return 角色
     */
    SysRole findByName(String name);

    /**
     * 查询角色表
     *
     * @return 角色列表
     */
    List<SysRole> findAllByOrderBySort();

    /**
     * 查询用户角色表
     *
     * @param roleId 角色ID
     * @return 数量
     */
    @Query(value = """
            SELECT COUNT(*)  FROM sys_users_roles WHERE role_id = :roleId
            """, nativeQuery = true)
    Long countByRoleId(Long roleId);

    /**
     * 查询用户角色表
     *
     * @param roleId 角色ID
     * @return 是否存在
     */
    default boolean existsByRoleId(Long roleId) {
        return countByRoleId(roleId) > 0;
    }
}
