package dev.rennen.beans.factory.support;

/**
 * @author rennen.dev
 * @since 2025/1/2 15:13
 */
public interface AutowiredCapableBeanFactory extends BeanFactory {
    int AUTOWIRE_NO = 0;
    int AUTOWIRE_BY_NAME = 1;
    int AUTOWIRE_BY_TYPE = 2;

    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName);

    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName);
}
