package com.admin.web.model;

import com.admin.web.annotation.SysUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author znn
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class SysBase implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "ID不能为空！", groups = {SysUpdate.class})
    private Long id;
    @CreatedBy
    @Column(name = "CREATE_USERNAME")
    private String createUsername;
    @CreatedDate
    @Column(name = "CREATE_TIMESTAMP", columnDefinition = "DATETIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimestamp;
    @LastModifiedBy
    @Column(name = "UPDATE_USERNAME")
    private String updateUsername;
    @LastModifiedDate
    @Column(name = "UPDATE_TIMESTAMP", columnDefinition = "DATETIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTimestamp;
    @Column(name = "REMARK")
    private String remark;
    @Column(name = "DISABLE")
    private boolean disable;

    public SysBase() {
        super();
        this.setId(null);
        this.setCreateUsername(null);
        this.setCreateTimestamp(null);
        this.setUpdateUsername(null);
        this.setUpdateTimestamp(null);
        this.setRemark(null);
        this.setDisable(false);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreateUsername() {
        return createUsername;
    }

    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    public LocalDateTime getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(LocalDateTime createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getUpdateUsername() {
        return updateUsername;
    }

    public void setUpdateUsername(String updateUsername) {
        this.updateUsername = updateUsername;
    }

    public LocalDateTime getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(LocalDateTime updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SysBase
                && Objects.equals(((SysBase) o).getId(), this.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return "SysBase{" +
                "id=" + this.getId() +
                ", createUsername=" + this.getCreateUsername() +
                ", createTimestamp=" + this.getCreateTimestamp() +
                ", updateUsername=" + this.getUpdateUsername() +
                ", updateTimestamp=" + this.getUpdateTimestamp() +
                ", remark='" + this.getRemark() + '\'' +
                ", disable='" + this.isDisable() + '\'' +
                '}';
    }
}
