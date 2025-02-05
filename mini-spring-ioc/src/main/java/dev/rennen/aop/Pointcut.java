package dev.rennen.aop;

/**
 * <br/> 2025/2/5 17:56
 *
 * @author rennen.dev
 */
public interface Pointcut {
    MethodMatcher getMethodMatcher();
}