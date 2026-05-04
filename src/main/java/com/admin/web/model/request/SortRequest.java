package com.admin.web.model.request;

import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

/**
 * @author znn
 */
public record SortRequest(String field, String order) {
    public static Sort by(SortRequest request) {
        return ObjectUtils.isEmpty(request)
                || ObjectUtils.isEmpty(request.field()) || ObjectUtils.isEmpty(request.order())
                ? Sort.unsorted() : Sort.by(Sort.Direction.fromString(request.order()), request.field());
    }
}
