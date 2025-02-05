package dev.rennen.aop;

import dev.rennen.util.PatternMatchUtils;

import java.lang.reflect.Method;

/**
 * <br/> 2025/2/5 17:57
 *
 * @author rennen.dev
 */
public class NameMatchMethodPointcut implements MethodMatcher, Pointcut {
    private String mappedName = "";

    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
    }

    @Override
    public boolean matches(Method method, Class<?> targetCLass) {
        return mappedName.equals(method.getName()) || isMatch(method.getName(), mappedName);
    }

    //核心方法，判断方法名是否匹配给定的模式
    protected boolean isMatch(String methodName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, methodName);
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

}