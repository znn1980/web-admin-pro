package com.admin.web.model.vo;

import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

/**
 * @author znn
 */
public class SortVo implements Serializable {
    private String field;
    private String order;

    public SortVo() {
        this.setField(null);
        this.setOrder(null);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "SortVo{" +
                "field='" + this.getField() + '\'' +
                ", order='" + this.getOrder() + '\'' +
                '}';
    }

    public static Sort by(SortVo vo) {
        return ObjectUtils.isEmpty(vo)
                || ObjectUtils.isEmpty(vo.getField()) || ObjectUtils.isEmpty(vo.getOrder())
                ? Sort.unsorted() : Sort.by(Sort.Direction.fromString(vo.getOrder()), vo.getField());
    }
}
