package dev.rennen.beans.factory;

import dev.rennen.beans.factory.aware.ApplicationContextAware;
import dev.rennen.beans.factory.process.BeanFactoryPostProcessor;
import dev.rennen.beans.factory.process.BeanPostProcessor;
import dev.rennen.beans.factory.support.ApplicationContext;
import dev.rennen.beans.factory.support.ConfigurableListableBeanFactory;
import dev.rennen.environment.Environment;
import dev.rennen.event.core.SimpleApplicationEventPublisher;
import dev.rennen.exception.BeansException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author rennen.dev
 * @since 2025/1/2 17:55
 */
public abstract class AbstractApplicationContext implements ApplicationContext {
    private Environment environment;

    @Getter
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    /**
     * 记录应用上下文启动的时间戳
     */

    private long startupDate;

    private final AtomicBoolean active = new AtomicBoolean(false);

    private final AtomicBoolean closed = new AtomicBoolean(false);

    @Setter
    @Getter
    private SimpleApplicationEventPublisher applicationEventPublisher;

    @Override
    public Object getBean(String name) {
        Object returnObj = getBeanFactory().getBean(name);
        if (returnObj instanceof ApplicationContextAware aware) {
            aware.setApplicationContext(this);
        }
        return returnObj;
    }

    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return getBeanFactory().isSingleton(name);
    }

    @Override
    public boolean isPrototype(String name) {
        return getBeanFactory().isPrototype(name);
    }

    @Override
    public Class<?> getType(String name) {
        return getBeanFactory().getType(name);
    }

    public void refresh() {
        postProcessBeanFactory(getBeanFactory());
        registerBeanPostProcessors(getBeanFactory());
        initApplicationEventPublisher();
        onRefresh();
        registerListeners();
        finishRefresh();
    }

    public abstract void finishRefresh();

    public abstract void registerListeners();

    public abstract void onRefresh();

    public abstract void initApplicationEventPublisher();

    public abstract void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory);

    public abstract void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        getBeanFactory().registerSingleton(beanName, singletonObject);
    }

    @Override
    public Object getSingleton(String beanName) {
        return getBeanFactory().getSingleton(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return getBeanFactory().containsSingleton(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return getBeanFactory().getSingletonNames();
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return getBeanFactory().getBeanDefinitionCount();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public String[] getBeanNamesByType(Class<?> type) {
        return getBeanFactory().getBeanNamesByType(type);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        getBeanFactory().addBeanPostProcessor(beanPostProcessor);
    }

    @Override
    public int getBeanPostProcessorCount() {
        return getBeanFactory().getBeanPostProcessorCount();
    }

    @Override
    public void registerDependentBean(String beanName, String dependentBeanName) {
        getBeanFactory().registerDependentBean(beanName, dependentBeanName);
    }

    @Override
    public String[] getDependentBeans(String beanName) {
        return getBeanFactory().getDependentBeans(beanName);
    }

    @Override
    public String[] getDependenciesForBean(String beanName) {
        return getBeanFactory().getDependenciesForBean(beanName);
    }

    @Override
    public String getApplicationName() {
        return "";
    }

    @Override
    public long getStartupDate() {
        return this.startupDate;
    }

    @Override
    public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Environment getEnvironment() {
        return this.environment;
    }

    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        this.beanFactoryPostProcessors.add(postProcessor);
    }


    @Override
    public void close() {
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void registerBean(@NonNull String beanName, Object obj) {
        getBeanFactory().registerBean(beanName, obj);
    }

}
