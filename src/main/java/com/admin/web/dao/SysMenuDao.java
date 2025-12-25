package com.admin.web.dao;

import com.admin.web.model.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author znn
 */
public interface SysMenuDao extends JpaRepository<SysMenu, Long>, JpaSpecificationExecutor<SysMenu> {
    /**
     * 查询菜单表
     *
     * @param title 菜单标题
     * @return 菜单
     */
    SysMenu findByTitle(String title);

    /**
     * 查询菜单表
     *
     * @return 菜单列表
     */
    List<SysMenu> findByOrderBySort();

    /**
     * 查询菜单表
     *
     * @param sysMenu 是否菜单
     * @return 菜单列表
     */
    List<SysMenu> findBySysMenuOrderBySort(boolean sysMenu);

    /**
     * 查询菜单表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Query(value = """
            SELECT sys_menu.*
            FROM sys_menu, sys_roles_menus, sys_role, sys_users_roles, sys_user
            WHERE sys_menu.id = sys_roles_menus.menu_id
            	AND sys_roles_menus.role_id = sys_role.id
            	AND sys_role.id = sys_users_roles.role_id
            	AND sys_users_roles.user_id = sys_user.id
            	AND sys_menu.sys_menu = true
            	AND sys_user.id = :userId
            ORDER BY sys_menu.sort
                        """, nativeQuery = true)
    List<SysMenu> findByUserIdOrderBySort(Long userId);

    /**
     * 查询菜单表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Query(value = """
            SELECT sys_menu.*
            FROM sys_menu, sys_role, sys_roles_menus, sys_user, sys_users_roles
            WHERE sys_menu.id = sys_roles_menus.menu_id
            	AND sys_roles_menus.role_id = sys_role.id
            	AND sys_role.id = sys_users_roles.role_id
            	AND sys_users_roles.user_id = sys_user.id
            	AND sys_menu.sys_menu = true
            	AND sys_menu.disable = false
            	AND sys_user.id = :userId
            ORDER BY sys_menu.sort
            """, nativeQuery = true)
    List<SysMenu> findByUserIdAndEnableOrderBySort(Long userId);

    /**
     * 查询菜单表
     *
     * @param pid 菜单PID
     * @return 菜单列表
     */
    List<SysMenu> findByPidOrderBySort(Long pid);

    /**
     * 查询菜单表
     *
     * @param pid 菜单PID
     * @return 是否存在
     */
    boolean existsByPid(Long pid);

    /**
     * 查询角色菜单表
     *
     * @param menuId 菜单ID
     * @return 数量
     */
    @Query(value = """
            SELECT COUNT(*)  FROM sys_roles_menus WHERE menu_id = :menuId
            """, nativeQuery = true)
    Long countByMenuId(Long menuId);

    /**
     * 查询角色菜单表
     *
     * @param menuId 菜单ID
     * @return 是否存在
     */
    default boolean existsByMenuId(Long menuId) {
        return countByMenuId(menuId) > 0;
    }

}
