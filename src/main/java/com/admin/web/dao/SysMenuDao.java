package com.admin.web.dao;

import com.admin.web.model.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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
}
