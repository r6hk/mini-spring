package dev.rennen.aop;

import dev.rennen.beans.factory.BeanFactoryAware;
import dev.rennen.beans.factory.FactoryBean;
import dev.rennen.beans.factory.support.BeanFactory;
import dev.rennen.exception.BeansException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;

/**
 * <br/> 2025/2/4 21:49
 *
 * @author rennen.dev
 */
@Slf4j
public class ProxyFactoryBean implements FactoryBean<Object>, BeanFactoryAware {
    @Getter
    @Setter
    private AopProxyFactory aopProxyFactory;
    private String[] interceptorNames;
    private String targetName;
    @Setter
    @Getter
    private Object target;
    private ClassLoader proxyClassLoader = getDefaultClassLoader();
    private Object singletonInstance;
    @Setter
    private BeanFactory beanFactory;
    @Setter
    private String interceptorName;
    private Advisor advisor;

    public ProxyFactoryBean() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    protected AopProxy createAopProxy() {
        return getAopProxyFactory().createAopProxy(target, this.advisor);
    }

    public void setInterceptorNames(String... interceptorNames) {
        this.interceptorNames = interceptorNames;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    /**
     * 获取内部代理对象，重要入口
     * @return 代理对象
     */
    @Override
    public Object getObject() {
        initializeAdvisor();
        return getSingletonInstance();
    }

    private synchronized Object getSingletonInstance() {//获取代理
        if (this.singletonInstance == null) {
            this.singletonInstance = getProxy(createAopProxy());
        }
        return this.singletonInstance;
    }

    protected Object getProxy(AopProxy aopProxy) {//生成代理对象
        return aopProxy.getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    private ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    private synchronized void initializeAdvisor() {
        Object advice = null;
        MethodInterceptor mi = null;
        try {
            advice = this.beanFactory.getBean(this.interceptorName);
        } catch (BeansException e) {
            log.error("Can't find interceptorName:{}", this.interceptorName);
        }
        this.advisor = new DefaultAdvisor();
        this.advisor.setMethodInterceptor((MethodInterceptor)advice);
    }
}