package com.admin.web.model.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * @author znn
 */
public record NoticeVo(
        State state,
        @NotNull(message = "页码不能为空！")
        @Min(value = 1, message = "页码不得小于壹！")
        Integer page,
        @NotNull(message = "每页数据不能为空！")
        @Min(value = 1, message = "每页数据不得小于壹！")
        @Max(value = 100, message = "每页数据不得大于壹佰！")
        Integer limit,
        SortVo sort
) {
    public enum State {
        /**
         * 未读，已读，我的，全部
         */
        UNREAD, READ, ME, ALL
    }
}
