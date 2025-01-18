package dev.rennen.webmvc.web;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

/**
 * 2025/1/8 17:17
 *
 * @author rennen.dev
 */
public class HandlerMethod {

    @Setter @Getter
    private Object bean;

    private Class<?> beanType;

    @Setter @Getter
    private Method method;

    private Class<?> returnType;

    private String description;

    private String className;

    private String methodName;

    public HandlerMethod(Method method, Object obj) {
        this.setMethod(method);
        this.setBean(obj);
    }

}