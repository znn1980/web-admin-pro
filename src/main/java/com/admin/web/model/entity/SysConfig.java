package com.admin.web.model.entity;

import com.admin.web.annotation.SysCreate;
import com.admin.web.annotation.SysUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author znn
 */
@Entity
@Table(name = "SYS_CONFIG")
public class SysConfig extends SysBase {
    @Column(name = "NAME")
    @NotBlank(message = "参数名称不能为空！", groups = {SysCreate.class, SysUpdate.class})
    @Size(min = 2, max = 32, message = "参数名称长度应在2-32之间！", groups = {SysCreate.class, SysUpdate.class})
    private String name;
    @Column(name = "`KEY`")
    @NotBlank(message = "参数键名不能为空！", groups = {SysCreate.class, SysUpdate.class})
    @Size(min = 1, max = 32, message = "参数键名长度应在1-32之间！", groups = {SysCreate.class, SysUpdate.class})
    private String key;
    @Column(name = "VALUE")
    @NotBlank(message = "参数键值不能为空！", groups = {SysCreate.class, SysUpdate.class})
    @Size(min = 1, max = 32, message = "参数键值长度应在1-32之间！", groups = {SysCreate.class, SysUpdate.class})
    private String value;

    public SysConfig() {
        super();
        this.setName(null);
        this.setKey(null);
        this.setValue(null);
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return super.toString() + System.lineSeparator() +
                "SysConfig{" +
                "name='" + this.getName() + '\'' +
                ", key='" + this.getKey() + '\'' +
                ", value='" + this.getValue() + '\'' +
                '}';
    }
}
