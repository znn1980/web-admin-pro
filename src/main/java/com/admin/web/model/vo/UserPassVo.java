package com.admin.web.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * @author znn
 */
public class UserPassVo implements Serializable {
    @NotBlank(message = "旧密码不能为空！")
    private String oldPassword;
    @NotBlank(message = "新密码不能为空！")
    @Size(min = 6, max = 36, message = "新密码长度应在6~36之间！")
    @Pattern(regexp = "^(?![a-zA-Z]+$)(?![A-Z\\d]+$)(?![A-Z\\W_]+$)(?![a-z\\d]+$)(?![a-z\\W_]+$)(?![\\d\\W_]+$)[a-zA-Z\\d\\W_]{6,36}$"
            , message = "新密码需为6~36位数字、大写字母、小写字母、符号等4种类型任选3种的组合，不含空格！")
    private String newPassword;
    @NotBlank(message = "确认密码不能为空！")
    private String confirmPassword;

    public UserPassVo() {
        this.setOldPassword(null);
        this.setNewPassword(null);
        this.setConfirmPassword(null);
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "UserPass{" +
                " oldPassword='" + "******" + '\'' +
                ", newPassword='" + "******" + '\'' +
                ", confirmPassword='" + "******" + '\'' +
                '}';
    }
}
