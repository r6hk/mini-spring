package dev.rennen.beans.factory.process;

import dev.rennen.beans.factory.support.BeanFactory;

/**
 * @author rennen.dev
 * @since 2025/1/2 10:31
 */
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName);

    Object postProcessAfterInitialization(Object bean, String beanName);

    void setBeanFactory(BeanFactory beanFactory);
}
