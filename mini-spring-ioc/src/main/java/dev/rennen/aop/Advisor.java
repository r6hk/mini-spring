package dev.rennen.aop;

/**
 * 持有一个 MethodInterceptor 用于拦截方法调用
 * <br/> 2025/2/5 10:03
 *
 * @author rennen.dev
 */
public interface Advisor {
    MethodInterceptor getMethodInterceptor();

    void setMethodInterceptor(MethodInterceptor methodInterceptor);
}