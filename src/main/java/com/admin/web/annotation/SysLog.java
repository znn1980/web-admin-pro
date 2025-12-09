package com.admin.web.annotation;

import java.lang.annotation.*;

/**
 * @author znn
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";
}
