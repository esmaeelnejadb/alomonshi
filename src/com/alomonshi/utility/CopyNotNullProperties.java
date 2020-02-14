package com.alomonshi.utility;

import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;

public class CopyNotNullProperties extends BeanUtilsBean {

    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if(value == null
                || (value instanceof Integer && (Integer) value == 0)
                || (value instanceof Boolean && !((boolean) value))
                || (value instanceof Float && (float) value == 0))
            return;
        super.copyProperty(dest, name, value);
    }
}
