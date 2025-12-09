package com.admin.web.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * @author znn
 */
public class PageQuery {
    @NotBlank(message = "页码不能为空！")
    @Min(value = 1, message = "页码不得小于零！")
    private Integer page;
    @NotBlank(message = "每页数据不能为空！")
    @Min(value = 1, message = "每页数据不得小于壹！")
    private Integer limit;

    public PageQuery() {
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
        return "PageQuery{" +
                "page=" + this.getPage() +
                ", limit=" + this.getLimit() +
                '}';
    }
}
