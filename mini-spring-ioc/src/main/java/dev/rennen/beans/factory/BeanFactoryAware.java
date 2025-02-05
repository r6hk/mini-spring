package dev.rennen.beans.factory;

import dev.rennen.beans.factory.support.BeanFactory;

/**
 * <br/> 2025/2/5 10:50
 *
 * @author rennen.dev
 */
public interface BeanFactoryAware {
    void setBeanFactory(BeanFactory beanFactory);
}
