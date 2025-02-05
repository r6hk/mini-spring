package dev.rennen.aop;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <br/> 2025/2/4 22:34
 *
 * @author rennen.dev
 */
@Slf4j
@AllArgsConstructor
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    Object target;
    PointcutAdvisor advisor;

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(JdkDynamicAopProxy.class.getClassLoader(),
                target.getClass().getInterfaces(),
                this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> targetClass = (target != null ? target.getClass() : null);
        if (this.advisor.getPointcut().getMethodMatcher().matches(method, targetClass)) {
            MethodInterceptor interceptor = this.advisor.getMethodInterceptor();
            MethodInvocation invocation =
                    new ReflectiveMethodInvocation(proxy, target, method, args, targetClass);
            return interceptor.invoke(invocation);
        }
        return null;
    }
}
