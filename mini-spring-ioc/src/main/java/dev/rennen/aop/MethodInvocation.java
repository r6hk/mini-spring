package dev.rennen.aop;

import java.lang.reflect.Method;

/**
 * 封装方法调用
 * <br/> 2025/2/5 9:59
 *
 * @author rennen.dev
 */
public interface MethodInvocation {
    Method getMethod();
    Object[] getArguments();
    Object getTarget();
    Object proceed() throws Throwable;
}
