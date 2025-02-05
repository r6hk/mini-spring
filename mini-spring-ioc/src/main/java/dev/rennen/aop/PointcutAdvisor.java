package dev.rennen.aop;

/**
 * <br/> 2025/2/5 17:57
 *
 * @author rennen.dev
 */
public interface PointcutAdvisor extends Advisor {
    Pointcut getPointcut();
}