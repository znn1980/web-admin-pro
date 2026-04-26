package com.admin.web.configuration;

import com.admin.web.model.SysMonitor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author znn
 */
@Configuration
@EnableConfigurationProperties({ConfigProperties.class})
public class AutoConfiguration {

    @Bean
    public SysMonitor sysMonitor() {
        return new SysMonitor();
    }
}
