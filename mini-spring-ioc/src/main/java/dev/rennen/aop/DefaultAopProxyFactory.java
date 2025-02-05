package dev.rennen.aop;

/**
 * <br/> 2025/2/4 22:40
 *
 * @author rennen.dev
 */
public class DefaultAopProxyFactory implements AopProxyFactory {
    @Override
    public AopProxy createAopProxy(Object target, PointcutAdvisor  advisor) {
        return new JdkDynamicAopProxy(target, advisor);
    }
}
