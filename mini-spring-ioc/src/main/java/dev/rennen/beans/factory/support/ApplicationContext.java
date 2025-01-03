package dev.rennen.beans.factory.support;

import dev.rennen.beans.factory.process.BeanFactoryPostProcessor;
import dev.rennen.environment.Environment;
import dev.rennen.environment.EnvironmentCapable;
import dev.rennen.event.core.ApplicationEventPublisher;

/**
 * @author rennen.dev
 * @since 2025/1/2 16:10
 */
public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, ConfigurableBeanFactory , ApplicationEventPublisher {
    String getApplicationName();

    long getStartupDate();

    ConfigurableListableBeanFactory getBeanFactory();

    void setEnvironment(Environment environment);

    Environment getEnvironment();

    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor);

    void refresh();

    void close();

    boolean isActive();
}
