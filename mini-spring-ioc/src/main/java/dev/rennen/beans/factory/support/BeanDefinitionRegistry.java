package dev.rennen.beans.factory.support;

import dev.rennen.beans.factory.config.BeanDefinition;

/**
 * @author rennen.dev
 * @since 2024/12/30 18:04
 */
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String name, BeanDefinition bd);

    void removeBeanDefinition(String name);

    BeanDefinition getBeanDefinition(String name);

    boolean containsBeanDefinition(String name);
}