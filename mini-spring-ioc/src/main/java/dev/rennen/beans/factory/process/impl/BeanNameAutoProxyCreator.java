package dev.rennen.beans.factory.process.impl;

import dev.rennen.aop.AopProxyFactory;
import dev.rennen.aop.DefaultAopProxyFactory;
import dev.rennen.aop.PointcutAdvisor;
import dev.rennen.aop.ProxyFactoryBean;
import dev.rennen.beans.factory.process.BeanPostProcessor;
import dev.rennen.beans.factory.support.BeanFactory;
import dev.rennen.exception.BeansException;
import dev.rennen.util.PatternMatchUtils;
import lombok.Setter;

/**
 * <br/> 2025/2/5 23:15
 *
 * @author rennen.dev
 */
public class BeanNameAutoProxyCreator implements BeanPostProcessor {
    @Setter
    String pattern; //代理对象名称模式，如action*
    private BeanFactory beanFactory;
    private AopProxyFactory aopProxyFactory;
    @Setter
    private String interceptorName;
    private PointcutAdvisor advisor;

    public BeanNameAutoProxyCreator() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    //核心方法。在bean实例化之后，init-method调用之前执行这个步骤。
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (isMatch(beanName, this.pattern)) {
            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean(); //创建以恶ProxyFactoryBean
            proxyFactoryBean.setTarget(bean);
            proxyFactoryBean.setBeanFactory(beanFactory);
            proxyFactoryBean.setAopProxyFactory(aopProxyFactory);
            proxyFactoryBean.setInterceptorName(interceptorName);
            return proxyFactoryBean;
        } else {
            return bean;
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    protected boolean isMatch(String beanName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, beanName);
    }
}