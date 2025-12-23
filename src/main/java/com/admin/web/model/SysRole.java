package com.admin.web.model;

import com.admin.web.annotation.SysCreate;
import com.admin.web.annotation.SysUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * @author znn
 */
@Entity
@Table(name = "SYS_ROLE")
public class SysRole extends BaseEntity {
    @Column(name = "NAME")
    @NotBlank(message = "角色名称不能为空！", groups = {SysCreate.class, SysUpdate.class})
    @Size(min = 2, max = 32, message = "角色名称长度应在2-32之间！")
    private String name;
    @Column(name = "SORT")
    private Long sort;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SYS_ROLES_MENUS",
            joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "MENU_ID", referencedColumnName = "ID")})
    private Set<SysMenu> menus;


    public SysRole() {
        super();
        this.setName(null);
        this.setSort(null);
        this.setMenus(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Set<SysMenu> getMenus() {
        return menus;
    }

    public void setMenus(Set<SysMenu> menus) {
        this.menus = menus;
    }


    @Override
    public String toString() {
        return super.toString() + System.lineSeparator() +
                "SysRole{" +
                "name='" + this.getName() + '\'' +
                ", sort=" + this.getSort() +
                ", menus=" + this.getMenus() +
                '}';
    }
}
