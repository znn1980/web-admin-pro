package com.admin.web.model.vo;

import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

/**
 * @author znn
 */
public record SortVo(String field, String order) {
    public static Sort by(SortVo vo) {
        return ObjectUtils.isEmpty(vo)
                || ObjectUtils.isEmpty(vo.field()) || ObjectUtils.isEmpty(vo.order())
                ? Sort.unsorted() : Sort.by(Sort.Direction.fromString(vo.order()), vo.field());
    }
}
