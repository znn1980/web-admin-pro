package com.admin.web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author znn
 */
@Entity
@Table(name = "SYS_MENU")
public class SysMenu extends BaseEntity {
    @Column(name = "PID")
    private Long pid;
    @Column(name = "TITLE")
    @NotBlank(message = "菜单标题不能为空！")
    @Size(min = 2, max = 64, message = "菜单标题长度应在2-64之间！")
    private String title;
    @Column(name = "METHOD")
    private String method;
    @Column(name = "URL")
    private String url;
    @Column(name = "ICON")
    private String icon;
    @Column(name = "SORT")
    private Long sort;
    @Column(name = "SYS_MENU")
    private boolean sysMenu;

    public SysMenu() {
        super();
        this.setPid(null);
        this.setTitle(null);
        this.setMethod(null);
        this.setUrl(null);
        this.setIcon(null);
        this.setSort(null);
        this.setSysMenu(true);
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public boolean isSysMenu() {
        return sysMenu;
    }

    public void setSysMenu(boolean sysMenu) {
        this.sysMenu = sysMenu;
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator() +
                "SysMenu{" +
                "pid=" + this.getPid() +
                ", title='" + this.getTitle() + '\'' +
                ", method='" + this.getMethod() + '\'' +
                ", url='" + this.getUrl() + '\'' +
                ", icon='" + this.getIcon() + '\'' +
                ", sort=" + this.getSort() +
                ", sysMenu=" + this.isSysMenu() +
                '}';
    }
}
