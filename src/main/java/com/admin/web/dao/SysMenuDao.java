package com.admin.web.dao;

import com.admin.web.model.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author znn
 */
public interface SysMenuDao extends JpaRepository<SysMenu, Long>, JpaSpecificationExecutor<SysMenu> {
    /**
     * 根据菜单标题查询
     *
     * @param title 菜单标题
     * @return 菜单
     */
    SysMenu findByTitle(String title);

    /**
     * 根据菜单的 PID 查询
     *
     * @param pid PID
     * @return 菜单
     */
    List<SysMenu> findByPidOrderBySort(Long pid);

    /**
     * 查询顶级菜单
     *
     * @return 菜单
     */
    List<SysMenu> findByPidIsNullOrderBySort();

}
