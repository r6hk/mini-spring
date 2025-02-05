package dev.rennen.aop;

import java.lang.reflect.Method;

/**
 * <br/> 2025/2/5 11:08
 *
 * @author rennen.dev
 */
public interface MethodBeforeAdvice extends BeforeAdvice {
    void before(Method method, Object[] args, Object target) throws Throwable;
}