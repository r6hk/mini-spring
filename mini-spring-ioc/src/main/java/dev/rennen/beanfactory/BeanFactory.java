package dev.rennen.beanfactory;

import dev.rennen.exception.BeansException;
import dev.rennen.beans.BeanDefinition;

/**
 * @author rennen.dev
 * @since 2024/12/27 17:30
 */
public interface BeanFactory {

    Object getBean(String beanName) throws BeansException;

    void registerBeanDefinition(BeanDefinition beanDefinition);

}
