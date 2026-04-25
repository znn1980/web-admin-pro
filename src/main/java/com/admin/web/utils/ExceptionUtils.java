package com.admin.web.utils;

import org.springframework.core.NestedExceptionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author znn
 */
public class ExceptionUtils extends NestedExceptionUtils {

    public static <T extends Throwable> T getCause(Throwable original, Class<T> clazz) {
        if (original == null || clazz == null) {
            return null;
        }
        Throwable cause = original.getCause();
        while (cause != null) {
            if (clazz.isInstance(cause)) {
                return clazz.cast(cause);
            }
            cause = cause.getCause();
        }
        return null;
    }

    public static String getStackTrace(Throwable e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
