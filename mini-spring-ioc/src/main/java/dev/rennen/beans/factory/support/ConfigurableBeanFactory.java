package dev.rennen.beans.factory.support;

import dev.rennen.beans.factory.config.SingletonBeanRegistry;
import dev.rennen.beans.factory.process.BeanPostProcessor;

/**
 * Configurable: 维护 Bean 之间的依赖关系，支持 Bean 处理器
 *
 * @author rennen.dev
 * @since 2025/1/2 11:58
 */
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    int getBeanPostProcessorCount();

    void registerDependentBean(String beanName, String dependentBeanName);

    String[] getDependentBeans(String beanName);

    String[] getDependenciesForBean(String beanName);
}