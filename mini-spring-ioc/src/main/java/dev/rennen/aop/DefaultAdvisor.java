package dev.rennen.aop;

import lombok.Getter;
import lombok.Setter;

/**
 * <br/> 2025/2/5 10:09
 *
 * @author rennen.dev
 */
@Setter
@Getter
public class DefaultAdvisor implements Advisor {
    private MethodInterceptor methodInterceptor;

    @Override
    public Advice getAdvice() {
        return this.methodInterceptor;
    }
}