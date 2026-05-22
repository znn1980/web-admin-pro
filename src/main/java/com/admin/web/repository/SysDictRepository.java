package com.admin.web.repository;

import com.admin.web.model.entity.SysDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author znn
 */
public interface SysDictRepository extends JpaRepository<SysDict, Long>, JpaSpecificationExecutor<SysDict> {
    /**
     * 根据名称查询字典
     *
     * @param name 名称
     * @return 字典
     */
    SysDict findByName(String name);

    /**
     * 根据键名查询字典
     *
     * @param key 键名
     * @return 字典
     */
    SysDict findByKey(String key);
}
