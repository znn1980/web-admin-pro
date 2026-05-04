package com.admin.web.model.request;

import jakarta.validation.constraints.NotBlank;

/**
 * @author znn
 */
public record UserLoginRequest(
        @NotBlank(message = "用户名不能为空！")
        String username,
        @NotBlank(message = "密码不能为空！")
        String password,
        @NotBlank(message = "验证码不能为空！")
        String sysCode
) {
    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        return String.format("LoginRequest[username=%s, password=******, sysCode=%s]"
                , this.username(), this.sysCode());
    }
}
