package dev.rennen.beans.factory.process.impl;

import dev.rennen.beans.factory.annotation.Autowired;
import dev.rennen.beans.factory.process.BeanPostProcessor;
import dev.rennen.beans.factory.support.BeanFactory;
import dev.rennen.exception.CreateBeanInstanceErrorException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;

/**
 * @author rennen.dev
 * @since 2025/1/2 10:32
 */
@Getter
@Setter
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Object result = bean;
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (ArrayUtils.isNotEmpty(fields)) {
            // 对每一个属性进行判断，如果带有 @Autowired 注解，则进行注入
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    // 获取属性的类型
                    String fieldName = field.getName();
                    Object value = getBeanFactory().getBean(fieldName);
                    try {
                        field.setAccessible(true);
                        field.set(bean, value);
                    } catch (IllegalAccessException e) {
                        throw new CreateBeanInstanceErrorException("Failed to inject bean '" + fieldName +
                                "' into bean '" + beanName + "'", e);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
