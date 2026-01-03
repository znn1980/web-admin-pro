package com.admin.web.model;

import com.admin.web.annotation.SysCreate;
import com.admin.web.annotation.SysUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author znn
 */
@Entity
@Table(name = "SYS_USER")
public class SysUser extends SysBase {
    @Column(name = "USERNAME")
    @NotBlank(message = "用户名不能为空！", groups = {SysCreate.class})
    @Size(min = 2, max = 32, message = "用户名长度应在2-32之间！", groups = {SysCreate.class})
    private String username;
    @Column(name = "PASSWORD")
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
    @Column(name = "PASS_TIMESTAMP", columnDefinition = "DATETIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime passTimestamp;
    @Column(name = "SYS_ADMIN")
    private boolean sysAdmin;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_USERS_ROLES",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    private Set<SysRole> roles;
    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private Set<SysNotice> notices;

    public SysUser() {
        super();
        this.setPassword(null);
        this.setMobile(null);
        this.setEmail(null);
        this.setAvatar(null);
        this.setPassTimestamp(null);
        this.setSysAdmin(false);
        this.setRoles(null);
        this.setNotices(this.getNotices());
    }

    public SysUser(Long id, String username) {
        this();
        this.setId(id);
        this.setUsername(username);
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

    public LocalDateTime getPassTimestamp() {
        return passTimestamp;
    }

    public void setPassTimestamp(LocalDateTime passTimestamp) {
        this.passTimestamp = passTimestamp;
    }

    public boolean isSysAdmin() {
        return sysAdmin;
    }

    public void setSysAdmin(boolean sysAdmin) {
        this.sysAdmin = sysAdmin;
    }

    public Set<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SysRole> roles) {
        this.roles = roles;
    }

    public Set<SysNotice> getNotices() {
        return notices;
    }

    public void setNotices(Set<SysNotice> notices) {
        this.notices = notices;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator() +
                "SysUser{" +
                "username='" + this.getUsername() + '\'' +
                ", password='" + this.getPassword() + '\'' +
                ", mobile='" + this.getMobile() + '\'' +
                ", email='" + this.getEmail() + '\'' +
                ", avatar='" + this.getAvatar() + '\'' +
                ", passTimestamp=" + this.getPassTimestamp() +
                ", sysAdmin=" + this.isSysAdmin() +
                '}';
    }
}
