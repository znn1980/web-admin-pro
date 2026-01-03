package com.admin.web.model;

import com.admin.web.annotation.SysCreate;
import com.admin.web.annotation.SysUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Set;

/**
 * @author znn
 */
@Entity
@Table(name = "SYS_NOTICE")
public class SysNotice extends SysBase {
    @Column(name = "TITLE")
    @NotBlank(message = "标题不能为空！", groups = {SysCreate.class, SysUpdate.class})
    @Size(min = 2, max = 255, message = "标题长度应在2-255之间！", groups = {SysCreate.class, SysUpdate.class})
    private String title;
    @Column(name = "CONTENT", columnDefinition = "TEXT")
    private String content;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SYS_NOTICES_USERS",
            joinColumns = {@JoinColumn(name = "NOTICE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")})
    private Set<SysUser> users;

    public SysNotice() {
        this.setTitle(null);
        this.setContent(null);
        this.setUsers(null);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<SysUser> getUsers() {
        return users;
    }

    public void setUsers(Set<SysUser> users) {
        this.users = users;
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
        return "SysNotice{" +
                "title='" + this.getTitle() + '\'' +
                ", content='" + StringUtils.truncate(Objects.toString(this.getContent())) + '\'' +
                '}';
    }
}
