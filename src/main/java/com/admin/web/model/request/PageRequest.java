package com.admin.web.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;

import java.util.Objects;

/**
 * @author znn
 */
public record PageRequest(
        @NotNull(message = "页码不能为空！")
        @Min(value = 1, message = "页码不得小于壹！")
        Integer page,
        @NotNull(message = "每页数据不能为空！")
        @Min(value = 1, message = "每页数据不得小于壹！")
        @Max(value = 100, message = "每页数据不得大于壹佰！")
        Integer limit,
        SortRequest sort
) {
    public static Pageable of(PageRequest request) {
        return Objects.isNull(request)
                ? org.springframework.data.domain.PageRequest.ofSize(10)
                : of(request.page(), request.limit(), request.sort());
    }

    public static Pageable of(Integer page, Integer limit, SortRequest sort) {
        return org.springframework.data.domain.PageRequest.of(Objects.requireNonNullElse(page, 1) - 1,
                Objects.requireNonNullElse(limit, 10), SortRequest.by(sort));
    }
}
