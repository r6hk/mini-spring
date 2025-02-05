package dev.rennen.aop;

/**
 * <br/> 2025/2/4 22:34
 *
 * @author rennen.dev
 */
public interface AopProxyFactory {
    AopProxy createAopProxy(Object target, PointcutAdvisor  advisor);
}
