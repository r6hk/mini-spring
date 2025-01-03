package dev.rennen.beans.factory.process;

import dev.rennen.beans.factory.support.BeanFactory;

/**
 * Bean 工厂后置处理器
 *
 * @author rennen.dev
 * @since 2025/1/3 16:46
 */
public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(BeanFactory beanFactory);
}
