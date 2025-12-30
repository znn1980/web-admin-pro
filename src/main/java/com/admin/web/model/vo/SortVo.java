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

    public static Sort by(SortVo sortVo) {
        return ObjectUtils.isEmpty(sortVo)
                || ObjectUtils.isEmpty(sortVo.getField()) || ObjectUtils.isEmpty(sortVo.getOrder())
                ? Sort.unsorted() : Sort.by(Sort.Direction.fromString(sortVo.getOrder()), sortVo.getField());
    }
}
