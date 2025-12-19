package com.admin.web.model.vo;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author znn
 */
public class UserLoginVo implements Serializable {
    @NotBlank(message = "用户名不能为空！")
    private String username;
    @NotBlank(message = "密码不能为空！")
    private String password;
    @NotBlank(message = "验证码不能为空！")
    private String sysCode;

    public UserLoginVo() {
        this.setUsername(null);
        this.setPassword(null);
        this.setSysCode(null);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    @Override
    public String toString() {
        return "UserLoginVo{" +
                "username='" + this.getUsername() + '\'' +
                ", password='" + this.getPassword() + '\'' +
                ", sysCode='" + this.getSysCode() + '\'' +
                '}';
    }
}
