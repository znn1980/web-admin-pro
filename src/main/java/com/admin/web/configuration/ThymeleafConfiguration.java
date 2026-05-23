package com.admin.web.configuration;

import com.admin.web.service.SysConfigurationsService;
import com.admin.web.service.SysDictionariesService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Set;

/**
 * @author znn
 */
@Configuration
public class ThymeleafConfiguration {

    @Bean
    public IExpressionObjectDialect expressionObjectDialect(IExpressionObjectFactory expressionObjectFactory) {
        return new IExpressionObjectDialect() {
            @Override
            public String getName() {
                return "Custom";
            }

            @Override
            public IExpressionObjectFactory getExpressionObjectFactory() {
                return expressionObjectFactory;
            }
        };
    }

    @Bean
    public IExpressionObjectFactory expressionObjectFactory(SysDictionariesService dictionariesService,
                                                            SysConfigurationsService configurationsService) {
        return new IExpressionObjectFactory() {
            public static final String DICTIONARIES_EXPRESSION_OBJECT_NAME = "dicts";
            public static final String CONFIGURATIONS_EXPRESSION_OBJECT_NAME = "configs";

            @Override
            public Set<String> getAllExpressionObjectNames() {
                return Set.of(DICTIONARIES_EXPRESSION_OBJECT_NAME, CONFIGURATIONS_EXPRESSION_OBJECT_NAME);
            }

            @Override
            public Object buildObject(IExpressionContext context, String expressionObjectName) {
                if (DICTIONARIES_EXPRESSION_OBJECT_NAME.equals(expressionObjectName)) {
                    return dictionariesService;
                }
                if (CONFIGURATIONS_EXPRESSION_OBJECT_NAME.equals(expressionObjectName)) {
                    return configurationsService;
                }
                return null;
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return true;
            }
        };
    }
}
