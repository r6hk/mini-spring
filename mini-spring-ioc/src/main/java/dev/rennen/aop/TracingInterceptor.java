package dev.rennen.aop;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * 在方法调用前后打印日志
 * <br/> 2025/2/5 10:00
 *
 * @author rennen.dev
 */
@Slf4j
public class TracingInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation i) throws Throwable {
        log.info("method {} is called on {} with args {}", i.getMethod(), i.getTarget(), Arrays.toString(i.getArguments()));
        Object ret = i.proceed();
        log.info("method {} returns {}", i.getMethod(), ret);
        return ret;
    }
}