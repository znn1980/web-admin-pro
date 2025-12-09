package com.admin.web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * @author znn
 */
//@Entity
//@Table(name = "SYS_ROLE")
public class SysRole extends BaseEntity {
    @Column(name = "NAME")
    @NotBlank(message = "角色名称不能为空！")
    @Size(min = 2, max = 32, message = "角色名称长度应在2-32之间！")
    private String name;
    @ManyToMany()
    @JoinTable(name = "SYS_ROLES_MENUS",
            joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "MENU_ID", referencedColumnName = "ID")})
    private List<SysMenu> menus;
    @ManyToMany(mappedBy = "roles")
    private List<SysUser> users;

    public SysRole() {
        super();
        this.setName(null);
        this.setMenus(null);
        this.setUsers(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SysMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<SysMenu> menus) {
        this.menus = menus;
    }

    public List<SysUser> getUsers() {
        return users;
    }

    public void setUsers(List<SysUser> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "SysRole{" +
                "name='" + this.getName() + '\'' +
               ", menus=" + this.getMenus() +
                ", users=" + this.getUsers() +
                '}';
    }
}
