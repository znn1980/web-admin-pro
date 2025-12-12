package com.admin.web.model.vo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * @author znn
 */
public class PageVo implements Serializable {
    @NotNull(message = "页码不能为空！")
    @Min(value = 1, message = "页码不得小于零！")
    private Integer page;
    @NotNull(message = "每页数据不能为空！")
    @Min(value = 1, message = "每页数据不得小于壹！")
    private Integer limit;

    public PageVo() {
        this.setPage(null);
        this.setLimit(null);
    }

    public Integer getPage() {
        return page - 1;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "PageVo{" +
                "page=" + this.getPage() +
                ", limit=" + this.getLimit() +
                '}';
    }
}
