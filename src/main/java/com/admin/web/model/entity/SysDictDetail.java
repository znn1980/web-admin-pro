package com.admin.web.model.entity;

import com.admin.web.annotation.SysCreate;
import com.admin.web.annotation.SysUpdate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author znn
 */
@Entity
@Table(name = "SYS_DICT_DETAIL")
public class SysDictDetail extends SysBase {
    @Column(name = "LABEL")
    @NotBlank(message = "字典标签不能为空！", groups = {SysCreate.class, SysUpdate.class})
    @Size(min = 1, max = 32, message = "字典标签长度应在1-32之间！", groups = {SysCreate.class, SysUpdate.class})
    private String label;
    @Column(name = "VALUE")
    @NotBlank(message = "字典数值不能为空！", groups = {SysCreate.class, SysUpdate.class})
    @Size(min = 1, max = 32, message = "字典键值长度应在1-32之间！", groups = {SysCreate.class, SysUpdate.class})
    private String value;
    @Column(name = "SORT")
    private Long sort;
    @ManyToOne(fetch = FetchType.LAZY)
    private SysDict dict;

    public SysDictDetail() {
        super();
        this.setLabel(null);
        this.setValue(null);
        this.setSort(null);
        this.setDict(null);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public SysDict getDict() {
        return dict;
    }

    public void setDict(SysDict dict) {
        this.dict = dict;
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
                "SysDictDetail{" +
                "label='" + this.getLabel() + '\'' +
                ", value='" + this.getValue() + '\'' +
                ", sort=" + this.getSort() +
                ", dict=" + this.getDict() +
                '}';
    }
}
