package com.admin.web.configuration;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author znn
 */
@Configuration
public class DefaultCaptchaConfiguration {

    @Bean
    public Config config() {
        return new Config(new Properties());
    }

    @Bean
    public DefaultKaptcha defaultKaptcha(Config config) {
        return new DefaultKaptcha() {{
            this.setConfig(config);
        }};
    }
}
