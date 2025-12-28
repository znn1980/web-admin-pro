package com.admin.web.model.vo;

import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Objects;

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
        return Objects.nonNull(sortVo.getField()) && Objects.nonNull(sortVo.getOrder())
                ? Sort.by(Sort.Direction.fromString(sortVo.getOrder()), sortVo.getField()) : Sort.unsorted();
    }
}
