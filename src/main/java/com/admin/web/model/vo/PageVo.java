package com.admin.web.model.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;

import java.util.Objects;

/**
 * @author znn
 */
public record PageVo(
        @NotNull(message = "页码不能为空！")
        @Min(value = 1, message = "页码不得小于壹！")
        Integer page,
        @NotNull(message = "每页数据不能为空！")
        @Min(value = 1, message = "每页数据不得小于壹！")
        @Max(value = 100, message = "每页数据不得大于壹佰！")
        Integer limit,
        SortVo sort
) {
    public static PageRequest of(PageVo vo) {
        return Objects.isNull(vo) ? PageRequest.ofSize(10) : of(vo.page(), vo.limit(), vo.sort());
    }

    public static PageRequest of(Integer page, Integer limit, SortVo sort) {
        return PageRequest.of(Objects.requireNonNullElse(page, 1) - 1,
                Objects.requireNonNullElse(limit, 10), SortVo.by(sort));
    }
}
