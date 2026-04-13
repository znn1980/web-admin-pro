package com.admin.web.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * @author znn
 */
public class WebUtils extends org.springframework.web.util.WebUtils {

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        for (String sub : Objects.requireNonNullElse(StringUtils.split(ip, ","), new String[0])) {
            ip = sub.trim();
            if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                break;
            }
        }
        return (Objects.equals("0:0:0:0:0:0:0:1", ip) || Objects.equals("::1", ip)) ? "127.0.0.1" : ip;
    }

    public static boolean isRequestRest(HttpServletRequest request) {
        return StringUtils.startsWithIgnoreCase(request.getHeader(HttpHeaders.ACCEPT)
                , MediaType.APPLICATION_JSON_VALUE)
                || StringUtils.startsWithIgnoreCase(request.getHeader(HttpHeaders.CONTENT_TYPE)
                , MediaType.APPLICATION_JSON_VALUE);
    }

    public static void setSession(HttpServletRequest request, String name, Object value) {
        setSessionAttribute(request, name, value);
    }

    public static <T> T getSession(HttpServletRequest request, String name, Class<T> clazz) {
        return clazz.cast(getSessionAttribute(request, name));
    }

    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static String getStackTrace(Throwable e) {
        try (StringWriter writer = new StringWriter()) {
            e.printStackTrace(new PrintWriter(writer, true));
            return writer.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
