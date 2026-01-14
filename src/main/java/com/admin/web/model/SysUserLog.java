package com.admin.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author znn
 */
@Entity
@Table(name = "SYS_USER_LOG")
public class SysUserLog implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "IP")
    private String ip;
    @Column(name = "OS")
    private String os;
    @Column(name = "BROWSER")
    private String browser;
    @Column(name = "METHOD")
    private String method;
    @Column(name = "URL")
    private String url;
    @Column(name = "NAME")
    private String name;
    @Column(name = "PARAMS", columnDefinition = "TEXT")
    private String params;
    @Column(name = "RESULT", columnDefinition = "TEXT")
    private String result;
    @Column(name = "ERRORS", columnDefinition = "TEXT")
    private String errors;
    @Column(name = "MS")
    private Long ms;
    @Column(name = "CREATE_TIMESTAMP", columnDefinition = "DATETIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public SysUserLog() {
        this.setId(null);
        this.setUsername(null);
        this.setIp(null);
        this.setOs(null);
        this.setBrowser(null);
        this.setMethod(null);
        this.setUrl(null);
        this.setName(null);
        this.setParams(null);
        this.setResult(null);
        this.setErrors(null);
        this.setMs(0L);
        this.setTimestamp(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public Long getMs() {
        return ms;
    }

    public void setMs(Long ms) {
        this.ms = ms;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "SysUserLog{" +
                "id=" + this.getId() +
                ", user=" + this.getUsername() +
                ", ip='" + this.getIp() + '\'' +
                ", os='" + this.getOs() + '\'' +
                ", browser='" + this.getBrowser() + '\'' +
                ", method='" + this.getMethod() + '\'' +
                ", url='" + this.getUrl() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", params='" + this.getParams() + '\'' +
                ", result='" + this.getResult() + '\'' +
                ", errors='" + this.getErrors() + '\'' +
                ", ms=" + this.getMs() +
                ", timestamp=" + this.getTimestamp() +
                '}';
    }
}
