package com.admin.web.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author znn
 */
public class ServerResponseEntity<T> implements Serializable {
    private String code;
    private String msg;
    private Long count;
    private T data;

    public ServerResponseEntity(String code, String msg, Long count, T data) {
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

    public static <T> ServerResponseEntity<T> ok() {
        return new ServerResponseEntity<>(ResponseCode.OK.value(), ResponseCode.OK.msg(), null, null);
    }

    public static <T> ServerResponseEntity<T> ok(T data) {
        return new ServerResponseEntity<>(ResponseCode.OK.value(), ResponseCode.OK.msg(), null, data);
    }

    public static <T> ServerResponseEntity<T> ok(Long count, T data) {
        return new ServerResponseEntity<>(ResponseCode.OK.value(), ResponseCode.OK.msg(), count, data);
    }

    public static <T> ServerResponseEntity<T> fail(String msg) {
        return new ServerResponseEntity<>(ResponseCode.FAIL.value(), msg, null, null);
    }

    public static <T> ServerResponseEntity<T> fail(ResponseCode responseCode) {
        return new ServerResponseEntity<>(responseCode.value(), responseCode.msg(), null, null);
    }

    public static <T> ServerResponseEntity<T> fail(String code, String msg) {
        return new ServerResponseEntity<>(code, msg, null, null);
    }

    @Override
    public String toString() {
        return "ServerResponseEntity{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", count=" + count +
                ", data=" + data +
                '}';
    }
}
