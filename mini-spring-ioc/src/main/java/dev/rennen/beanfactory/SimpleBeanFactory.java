package dev.rennen.beanfactory;

import dev.rennen.beans.define.BeanDefinition;
import dev.rennen.exception.NoSuchBeanDefinitionException;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author rennen.dev
 * @since 2024/12/27 17:55
 */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();


    //getBean，容器的核心方法
    @Override
    public Object getBean(@NonNull String beanName) {
        //先尝试直接拿Bean实例
        Object singleton = this.getSingleton(beanName);
        //如果此时还没有这个Bean的实例，则获取它的定义来创建实例
        if (singleton == null) {
            //获取Bean的定义
            BeanDefinition beanDefinition = Optional.ofNullable(beanDefinitionMap.get(beanName))
                    .orElseThrow(NoSuchBeanDefinitionException::new);
            try {
                singleton = Class.forName(beanDefinition.getClassName()).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("instance bean error", e);
            }
            //注册Bean实例
            registerSingleton(beanName, singleton);
        }
        return singleton;
    }

    @Override
    public Boolean containsBean(@NonNull String name) {
        return containsSingleton(name);
    }

    @Override
    public void registerBean(@NonNull String beanName, Object obj) {
        registerSingleton(beanName, obj);
    }

    @Override
    public boolean isSingleton(@NonNull String name) {
        return Optional.ofNullable(beanDefinitionMap.get(name))
                .map(BeanDefinition::getScope)
                .map(BeanDefinition.SCOPE_SINGLETON::equals)
                .orElseThrow(NoSuchBeanDefinitionException::new);
    }

    @Override
    public boolean isPrototype(@NonNull String name) {
        return Optional.ofNullable(beanDefinitionMap.get(name))
                .map(BeanDefinition::getScope)
                .map(BeanDefinition.SCOPE_PROTOTYPE::equals)
                .orElseThrow(NoSuchBeanDefinitionException::new);
    }

    @Override
    public Class<?> getType(@NonNull String name) {
        return null;
    }


    public void registerBeanDefinition(@NonNull BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanDefinition.getId(), beanDefinition);
    }
}
