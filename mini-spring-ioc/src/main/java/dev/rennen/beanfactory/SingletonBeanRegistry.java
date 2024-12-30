package dev.rennen.beanfactory;

/**
 * @author rennen.dev
 * @since 2024/12/30 16:37
 */
public interface SingletonBeanRegistry {

    void registerSingleton(String beanName, Object singletonObject);

    Object getSingleton(String beanName);

    boolean containsSingleton(String beanName);

    String[] getSingletonNames();

}