package com.admin.web.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.*;

/**
 * @author znn
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    public static void copyNonNullProperties(Object source, Object target) {
        copyProperties(source, target, getNullProperties(source));
    }

    public static String[] getNullProperties(Object source) {
        Set<String> properties = new HashSet<>();
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        Arrays.asList(beanWrapper.getPropertyDescriptors()).forEach(descriptor -> {
            if (Objects.isNull(beanWrapper.getPropertyValue(descriptor.getName()))) {
                properties.add(descriptor.getName());
            }
        });
        return properties.toArray(new String[0]);
    }

}
