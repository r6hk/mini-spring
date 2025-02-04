package dev.rennen.aop;

import dev.rennen.beans.factory.FactoryBean;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ClassUtils;

/**
 * <br/> 2025/2/4 21:49
 *
 * @author rennen.dev
 */
public class ProxyFactoryBean implements FactoryBean<Object> {
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

    public ProxyFactoryBean() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    protected AopProxy createAopProxy() {
        return getAopProxyFactory().createAopProxy(target);
    }

    public void setInterceptorNames(String... interceptorNames) {
        this.interceptorNames = interceptorNames;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    @Override
    public Object getObject() throws Exception {//获取内部对象
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
}