package com.admin.web.repository;

import com.admin.web.model.entity.SysDict;
import com.admin.web.model.entity.SysDictDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author znn
 */
public interface SysDictDetailRepository extends JpaRepository<SysDictDetail, Long>, JpaSpecificationExecutor<SysDictDetail> {
    /**
     * 根据标签查询字典详情
     *
     * @param label 标签
     * @return 字典详情
     */
    SysDictDetail findByLabel(String label);

    /**
     * 根据值查询字典详情
     *
     * @param value 值
     * @return 字典详情
     */
    SysDictDetail findByValue(String value);

    /**
     * 根据字典查询字典详情
     *
     * @param dict 字典
     * @return 字典详情
     */
    List<SysDictDetail> findByDictOrderBySort(SysDict dict);
}
