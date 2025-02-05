package dev.rennen.aop;

/**
 * <br/> 2025/2/5 16:58
 *
 * @author rennen.dev
 */
public class AfterReturningAdviceInterceptor implements MethodInterceptor, AfterAdvice {
    private final AfterReturningAdvice advice;

    public AfterReturningAdviceInterceptor(AfterReturningAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getTarget());
        return retVal;
    }
}
