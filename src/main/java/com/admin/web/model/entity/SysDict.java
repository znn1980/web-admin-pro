package com.admin.web.model.entity;

import com.admin.web.annotation.SysCreate;
import com.admin.web.annotation.SysUpdate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * @author znn
 */
@Entity
@Table(name = "SYS_DICT")
public class SysDict extends SysBase {
    @Column(name = "NAME")
    @NotBlank(message = "字典名称不能为空！", groups = {SysCreate.class, SysUpdate.class})
    @Size(min = 2, max = 32, message = "字典名称长度应在2-32之间！", groups = {SysCreate.class, SysUpdate.class})
    private String name;
    @Column(name = "`KEY`")
    @NotBlank(message = "字典键名不能为空！", groups = {SysCreate.class, SysUpdate.class})
    @Size(min = 1, max = 32, message = "字典键名长度应在1-32之间！", groups = {SysCreate.class, SysUpdate.class})
    private String key;
    @OneToMany(mappedBy = "dict", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnore
    private Set<SysDictDetail> details;

    public SysDict() {
        super();
        this.setName(null);
        this.setKey(null);
        this.setDetails(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Set<SysDictDetail> getDetails() {
        return details;
    }

    public void setDetails(Set<SysDictDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator() +
                "SysDict{" +
                "name='" + this.getName() + '\'' +
                ", key='" + this.getKey() + '\'' +
                ", details=" + this.getDetails() +
                '}';
    }
}
