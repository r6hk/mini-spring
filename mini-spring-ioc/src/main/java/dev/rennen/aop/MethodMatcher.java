package dev.rennen.aop;

import java.lang.reflect.Method;

/**
 * <br/> 2025/2/5 17:55
 *
 * @author rennen.dev
 */
public interface MethodMatcher {
    boolean matches(Method method, Class<?> targetCLass);
}