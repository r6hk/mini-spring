package dev.rennen.beans.factory;

import dev.rennen.beans.factory.config.BeanDefinition;
import dev.rennen.beans.factory.support.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rennen.dev
 * @since 2025/1/2 15:32
 */
public class DefaultListableBeanFactory extends AbstractAutowiredCapableBeanFactory implements ConfigurableListableBeanFactory {

    @Override
    public void registerDependentBean(String beanName, String dependentBeanName) {

    }

    @Override
    public String[] getDependentBeans(String beanName) {
        return new String[0];
    }

    @Override
    public String[] getDependenciesForBean(String beanName) {
        return new String[0];
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

    @Override
    public String[] getBeanNamesByType(Class<?> type) {
        List<String> result = new ArrayList<>();
        for (BeanDefinition bd : beanDefinitionMap.values()) {
            if (type.isAssignableFrom(bd.getClass())) {
                result.add(bd.getId());
            }
        }
        return result.toArray(new String[0]);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        String[] beanNames = getBeanNamesByType(type);
        Map<String, T> result = new LinkedHashMap<>(beanNames.length);
        for (String beanName : beanNames) {
            result.put(beanName, (T) getBean(beanName));
        }
        return result;
    }
}
