package dev.rennen.beans.factory.support;

/**
 * @author rennen.dev
 * @since 2024/12/27 17:30
 */
public interface BeanFactory {

    Object getBean(String beanName);

    boolean containsBean(String name);

    void registerBean(String beanName, Object obj);

    boolean isSingleton(String name);

    boolean isPrototype(String name);

    Class<?> getType(String name);
}
