package dev.rennen.aop;

/**
 * 方法上的拦截器
 * <br/> 2025/2/5 9:58
 *
 * @author rennen.dev
 */
public interface MethodInterceptor extends Interceptor {
    Object invoke(MethodInvocation invocation) throws Throwable;
}