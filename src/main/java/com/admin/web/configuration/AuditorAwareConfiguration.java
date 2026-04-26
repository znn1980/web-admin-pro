package com.admin.web.configuration;

import com.admin.web.utils.SecurityUtils;
import com.admin.web.utils.WebUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Objects;
import java.util.Optional;

/**
 * @author znn
 */
@Configuration
@EnableJpaAuditing
public class AuditorAwareConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(
                Objects.requireNonNullElse(
                        SecurityUtils.getSysUser(WebUtils.getRequest()),
                        SecurityUtils.getSysUser()
                ).getUsername()
        );
    }
}
