package com.admin.web.model.vo;

import jakarta.validation.constraints.NotBlank;

/**
 * @author znn
 */
public record UserLoginVo(
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
        return String.format("UserLoginVo[username=%s, password=******, sysCode=%s]"
                , this.username(), this.sysCode());
    }
}
