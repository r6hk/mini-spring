package dev.rennen.beans.factory;

import dev.rennen.exception.BeansException;
import lombok.extern.slf4j.Slf4j;

/**
 * <br/> 2025/2/4 21:23
 *
 * @author rennen.dev
 */
@Slf4j
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {
    protected Class<?> getTypeForFactoryBean(final FactoryBean<?> factoryBean) {
        return factoryBean.getObjectType();
    }

    protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName) {
        Object object = doGetObjectFromFactoryBean(factory, beanName);
        try {
            object = postProcessObjectFromFactoryBean(object, beanName);
        } catch (BeansException e) {
            log.error("postProcessObjectFromFactoryBean error", e);
        }
        return object;
    }

    //从factory bean中获取内部包含的对象
    private Object doGetObjectFromFactoryBean(final FactoryBean<?> factory, final String beanName) {
        Object object = null;
        try {
            object = factory.getObject();
        } catch (Exception e) {
            log.error("getObjectFromFactoryBean error", e);
        }
        return object;
    }

    protected Object postProcessObjectFromFactoryBean(Object object, String beanName) throws BeansException {
        // 默认情况下不实现，子类可以覆盖此方法以添加自定义处理
        return object;
    }
}
