package com.admin.web.model;

import com.admin.web.annotation.SysCreate;
import com.admin.web.annotation.SysLogin;
import com.admin.web.annotation.SysPassword;
import com.admin.web.annotation.SysUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author znn
 */
@Entity
@Table(name = "SYS_USER")
public class SysUser extends BaseEntity {
    @Column(name = "USERNAME")
    @NotBlank(message = "用户名不能为空！", groups = {SysLogin.class, SysCreate.class})
    @Size(min = 2, max = 32, message = "用户名长度应在2-32之间！", groups = {SysCreate.class})
    private String username;
    @Column(name = "PASSWORD")
    @NotBlank(message = "密码不能为空！", groups = {SysLogin.class})
    private String password;
    @Column(name = "MOBILE", length = 11)
    @NotBlank(message = "手机号码不能为空！", groups = {SysCreate.class, SysUpdate.class})
    @Size(min = 8, max = 11, message = "手机号码长度应在8-11之间！", groups = {SysCreate.class, SysUpdate.class})
    private String mobile;
    @Column(name = "EMAIL")
    @NotBlank(message = "邮箱地址不能为空！", groups = {SysCreate.class, SysUpdate.class})
    @Email(message = "邮箱地址不正确！", groups = {SysCreate.class, SysUpdate.class})
    private String email;
    @Column(name = "AVATAR")
    private String avatar;
    @Column(name = "PASSWORD_TIMESTAMP", columnDefinition = "DATETIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime passwordTimestamp;
    @Column(name = "SYS_ADMIN")
    private boolean sysAdmin;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SYS_USERS_ROLES",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    private List<SysRole> roles;
    @Transient
    private String sessionId;
    @Transient
    private String ip;
    @Transient
    private String oldPassword;
    @Size(min = 6, max = 36, message = "密码长度应在6-36之间！", groups = {SysPassword.class})
    @Pattern(regexp = "^(?![a-zA-Z]+$)(?![A-Z\\d]+$)(?![A-Z\\W_]+$)(?![a-z\\d]+$)(?![a-z\\W_]+$)(?![\\d\\W_]+$)[a-zA-Z\\d\\W_]{6,36}$"
            , message = "密码需为6-36位数字、大写字母、小写字母、符号等4种类型任选3种的组合，不含空格！", groups = {SysPassword.class})
    @Transient
    private String newPassword;
    @Transient
    private String confirmPassword;
    @NotBlank(message = "验证码不能为空！", groups = {SysLogin.class})
    @Transient
    private String sysCode;

    public SysUser() {
        super();
    }

    public SysUser(Long id, String username) {
        this.setId(id);
        this.setUsername(username);
        this.setPassword(null);
        this.setMobile(null);
        this.setEmail(null);
        this.setAvatar(null);
        this.setPasswordTimestamp(null);
        this.setSysAdmin(false);
        this.setRoles(null);
        this.setSessionId(null);
        this.setIp(null);
        this.setOldPassword(null);
        this.setNewPassword(null);
        this.setConfirmPassword(null);
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDateTime getPasswordTimestamp() {
        return passwordTimestamp;
    }

    public void setPasswordTimestamp(LocalDateTime passwordTimestamp) {
        this.passwordTimestamp = passwordTimestamp;
    }

    public boolean isSysAdmin() {
        return sysAdmin;
    }

    public void setSysAdmin(boolean sysAdmin) {
        this.sysAdmin = sysAdmin;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    @Override
    public String toString() {
        return "SysUser{" +
                "username='" + this.getUsername() + '\'' +
                ", password='" + this.getPassword() + '\'' +
                ", mobile='" + this.getMobile() + '\'' +
                ", email='" + this.getEmail() + '\'' +
                ", avatar='" + this.getAvatar() + '\'' +
                ", passwordTimestamp=" + this.getPasswordTimestamp() +
                ", sysAdmin=" + this.isSysAdmin() +
                //", roles=" + this.getRoles() +
                ", sessionId='" + this.getSessionId() + '\'' +
                ", ip='" + this.getIp() + '\'' +
                ", oldPassword='" + this.getOldPassword() + '\'' +
                ", newPassword='" + this.getNewPassword() + '\'' +
                ", confirmPassword='" + this.getConfirmPassword() + '\'' +
                ", sysCode='" + this.getSysCode() + '\'' +
                '}';
    }
}
