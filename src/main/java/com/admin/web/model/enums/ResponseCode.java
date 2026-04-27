package com.admin.web.model.enums;

/**
 * @author znn
 */
public enum ResponseCode {
    /**
     * 响应码
     */
    OK("0", "成功！"),
    FAIL("9000", "失败！"),
    LOGOUT("1001", "抱歉，登录状态失效！"),
    DENIED("4003", "抱歉，您无权访问！"),
    NOT_FOUND("4004", "抱歉，资源不存在！"),
    ERROR("9999", "抱歉，服务器出错了！");
    private final String code, msg;

    public String code() {
        return code;
    }

    public String msg() {
        return msg;
    }

    ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
