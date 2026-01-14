package com.admin.web.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author znn
 */
public class WebUtils extends org.springframework.web.util.WebUtils {
    static final String UNKNOWN = "unknown";
    static final String COMMA = ",";

    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.hasText(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (!StringUtils.hasText(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.split(COMMA)[0].trim();
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
}
