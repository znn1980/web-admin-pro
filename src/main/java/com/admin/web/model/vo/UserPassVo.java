package com.admin.web.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * @author znn
 */
public record UserPassVo(
        @NotBlank(message = "旧密码不能为空！")
        String oldPassword,
        @NotBlank(message = "新密码不能为空！")
        @Size(min = 6, max = 36, message = "新密码长度应在6~36之间！")
        @Pattern(regexp = "^(?![a-zA-Z]+$)(?![A-Z\\d]+$)(?![A-Z\\W_]+$)(?![a-z\\d]+$)(?![a-z\\W_]+$)(?![\\d\\W_]+$)[a-zA-Z\\d\\W_]{6,36}$"
                , message = "新密码需为6~36位数字、大写字母、小写字母、符号等4种类型任选3种的组合，不含空格！")
        String newPassword,
        @NotBlank(message = "确认密码不能为空！")
        String confirmPassword
) {
    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        return "UserPassVo[oldPassword=******, newPassword=******, confirmPassword=******]";
    }
}
