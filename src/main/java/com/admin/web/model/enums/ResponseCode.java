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
    LOGOUT("1001", "登录状态失效！"),
    DENIED("1002", "抱歉，您无权访问！"),
    ERROR("9999", "抱歉，服务器出错了！");
    private final String code;

    private final String msg;

    public String value() {
        return code;
    }

    public String msg() {
        return msg;
    }

    ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResponseCode{"
                + "code='" + this.value() + '\''
                + ", msg='" + this.msg() + '\''
                + "} "
                + super.toString();
    }
}
