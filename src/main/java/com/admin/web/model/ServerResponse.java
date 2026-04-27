package com.admin.web.model;

import com.admin.web.model.enums.ResponseCode;
import org.springframework.validation.BindingResult;

import java.util.Objects;

/**
 * @author znn
 */
public record ServerResponse<T>(String code, String msg, Long count, T data) {

    public boolean isSuccess() {
        return Objects.equals(ResponseCode.OK.code(), this.code);
    }

    public static <T> ServerResponse<T> ok() {
        return new ServerResponse<>(ResponseCode.OK.code(), ResponseCode.OK.msg(), null, null);
    }

    public static <T> ServerResponse<T> ok(T data) {
        return new ServerResponse<>(ResponseCode.OK.code(), ResponseCode.OK.msg(), null, data);
    }

    public static <T> ServerResponse<T> ok(Long count, T data) {
        return new ServerResponse<>(ResponseCode.OK.code(), ResponseCode.OK.msg(), count, data);
    }

    public static <T> ServerResponse<T> fail(String msg) {
        return new ServerResponse<>(ResponseCode.FAIL.code(), msg, null, null);
    }

    public static <T> ServerResponse<T> fail(String msg, Object... args) {
        return new ServerResponse<>(ResponseCode.FAIL.code(), String.format(msg, args), null, null);
    }

    public static <T> ServerResponse<T> fail(ResponseCode responseCode) {
        return new ServerResponse<>(responseCode.code(), responseCode.msg(), null, null);
    }

    public static <T> ServerResponse<T> fail(BindingResult bindingResult) {
        return new ServerResponse<>(ResponseCode.FAIL.code(), Objects.isNull(bindingResult.getFieldError())
                ? ResponseCode.FAIL.msg() : bindingResult.getFieldError().getDefaultMessage(), null, null);
    }
}
