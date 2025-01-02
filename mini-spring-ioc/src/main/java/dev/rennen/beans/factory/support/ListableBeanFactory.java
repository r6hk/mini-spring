package dev.rennen.beans.factory.support;

import java.util.Map;

/**
 * 将 Factory 内部管理的 Bean 作为一个集合来对待，提供相关方法
 * @author rennen.dev
 * @since 2025/1/2 11:58
 */
public interface ListableBeanFactory extends BeanFactory {
    /**
     * 获取所有 Bean 的名称
     * @return 所有 Bean 的名称
     */
    String[] getBeanDefinitionNames();

    /**
     * 获取所有 Bean 的数量
     * @return 所有 Bean 的数量
     */
    int getBeanDefinitionCount();

    /**
     * 获取指定类型的 Bean 的名称
     * @param type 类型
     * @return 指定类型的 Bean 的名称
     */
    String[] getBeanNamesByType (Class<?> type);

    /**
     * 判断是否包含指定名称的 Bean
     *
     * @param beanName Bean 名称
     * @return 是否包含指定名称的 Bean
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 获取指定类型的 Bean
     *
     * @param type 类型
     * @param <T> 类型
     * @return 指定类型的 Bean
     */
    <T> Map<String, T> getBeansOfType(Class<T> type);
}
