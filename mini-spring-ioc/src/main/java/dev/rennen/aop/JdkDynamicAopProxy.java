package dev.rennen.aop;

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
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    Object target;
    public JdkDynamicAopProxy(Object target) {
        this.target = target;
    }
    @Override
    public Object getProxy() {
        ClassLoader classLoader = JdkDynamicAopProxy.class.getClassLoader();
        Class<?>[] interfaces = target.getClass().getInterfaces();
        return Proxy.newProxyInstance(classLoader, interfaces, this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("doAction")) {
            log.info("-----before call real object, dynamic proxy........");
            if (args[0] instanceof Boolean booleanArg) {
                args[0] = !booleanArg;
            }
        }
        return method.invoke(target, args);
    }
}
