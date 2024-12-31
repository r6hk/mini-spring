package dev.rennen.beanfactory;

/**
 * @author rennen.dev
 * @since 2024/12/27 17:30
 */
public interface BeanFactory {

    Object getBean(String beanName);

    Boolean containsBean(String name);

    void registerBean(String beanName, Object obj);

    boolean isSingleton(String name);

    boolean isPrototype(String name);

    Class<?> getType(String name);
}
