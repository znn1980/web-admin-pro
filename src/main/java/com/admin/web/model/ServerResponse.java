package com.admin.web.model;

import com.admin.web.model.enums.ResponseCode;
import org.springframework.validation.BindingResult;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author znn
 */
public class ServerResponse<T> implements Serializable {
    private String code;
    private String msg;
    private Long count;
    private T data;

    public ServerResponse(String code, String msg, Long count, T data) {
        this.setCode(code);
        this.setMsg(msg);
        this.setCount(count);
        this.setData(data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return Objects.equals(ResponseCode.OK.value(), this.code);
    }

    public static <T> ServerResponse<T> ok() {
        return new ServerResponse<>(ResponseCode.OK.value(), ResponseCode.OK.msg(), null, null);
    }

    public static <T> ServerResponse<T> ok(T data) {
        return new ServerResponse<>(ResponseCode.OK.value(), ResponseCode.OK.msg(), null, data);
    }

    public static <T> ServerResponse<T> ok(Long count, T data) {
        return new ServerResponse<>(ResponseCode.OK.value(), ResponseCode.OK.msg(), count, data);
    }

    public static <T> ServerResponse<T> fail(String msg) {
        return new ServerResponse<>(ResponseCode.FAIL.value(), msg, null, null);
    }

    public static <T> ServerResponse<T> fail(String msg, Object... args) {
        return new ServerResponse<>(ResponseCode.FAIL.value(), String.format(msg, args), null, null);
    }

    public static <T> ServerResponse<T> fail(ResponseCode responseCode) {
        return new ServerResponse<>(responseCode.value(), responseCode.msg(), null, null);
    }

    public static <T> ServerResponse<T> fail(BindingResult bindingResult) {
        return new ServerResponse<>(ResponseCode.FAIL.value(), bindingResult.getFieldError().getDefaultMessage(), null, null);
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "code='" + this.getCode() + '\'' +
                ", msg='" + this.getMsg() + '\'' +
                ", count=" + this.getCount() +
                ", data=" + this.getData() +
                '}';
    }
}
