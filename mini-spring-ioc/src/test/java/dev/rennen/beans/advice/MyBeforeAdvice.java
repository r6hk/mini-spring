package dev.rennen.beans.advice;

import dev.rennen.aop.MethodBeforeAdvice;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * <br/> 2025/2/5 17:10
 *
 * @author rennen.dev
 */
@Slf4j
public class MyBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        log.info("**MyBeforeAdvice**   method {} is called on {} with args {}", method, target, args);
    }
}
