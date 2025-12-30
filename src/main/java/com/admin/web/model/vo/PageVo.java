package com.admin.web.model.vo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;

import java.io.Serializable;
import java.util.Objects;

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
    private SortVo sort;

    public PageVo() {
        this.setPage(null);
        this.setLimit(null);
        this.setSort(null);
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

    public SortVo getSort() {
        return sort;
    }

    public void setSort(SortVo sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "PageVo{" +
                "page=" + this.getPage() +
                ", limit=" + this.getLimit() +
                ", sort=" + this.getSort() +
                '}';
    }

    public static PageRequest of(PageVo pageVo) {
        return Objects.isNull(pageVo) ? PageRequest.ofSize(10)
                : PageRequest.of(pageVo.getPage(), pageVo.getLimit(), SortVo.by(pageVo.getSort()));
    }
}
