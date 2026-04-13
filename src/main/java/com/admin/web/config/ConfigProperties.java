package com.admin.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author znn
 */
@ConfigurationProperties(prefix = ConfigProperties.CONFIG_PREFIX)
public class ConfigProperties {
    public static final String CONFIG_PREFIX = "config";
    public static final String DEFAULT_NAME = "WEB-ADMIN-PRO";
    public static final String DEFAULT_VERSION = "1.0.0";
    public static final String DEFAULT_COPYRIGHT = "© 2025 All Rights Reserved.";
    private String name = DEFAULT_NAME;
    private String version = DEFAULT_VERSION;
    private String copyright = DEFAULT_COPYRIGHT;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}
