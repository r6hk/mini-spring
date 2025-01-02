package dev.rennen.beans.factory;

import dev.rennen.beans.factory.config.BeanDefinition;
import dev.rennen.beans.factory.config.ConstructorArgumentValue;
import dev.rennen.beans.factory.config.ConstructorArgumentValues;
import dev.rennen.beans.factory.support.BeanDefinitionRegistry;
import dev.rennen.beans.factory.support.BeanFactory;
import dev.rennen.beans.inject.PropertyValue;
import dev.rennen.beans.inject.PropertyValues;
import dev.rennen.exception.BeansException;
import dev.rennen.exception.CreateBeanInstanceErrorException;
import dev.rennen.exception.NoSuchBeanDefinitionException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author rennen.dev
 * @since 2025/1/2 10:38
 */
@Slf4j
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {

    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    protected final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();

    //getBean，容器的核心方法
    @Override
    public Object getBean(String beanName) throws BeansException {
        // 先尝试从容器中获取单例实例
        Object singleton = this.getSingleton(beanName);
        if (singleton == null) {
            synchronized (this) {
                // 双重检查，避免重复创建
                singleton = this.getSingleton(beanName);
                if (singleton == null) {
                    // 从毛胚实例中获取
                    singleton = this.earlySingletonObjects.get(beanName);
                    if (singleton == null) {
                        // 如果毛胚也不存在，则创建实例
                        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                        if (beanDefinition == null) {
                            throw new BeansException("No bean named '" + beanName + "' available");
                        }
                        singleton = createBean(beanDefinition);
                        this.registerSingleton(beanName, singleton);
                        // 进行 BeanPostProcessor 处理
                        // step 1: postProcessBeforeInitialization
                        applyBeanPostProcessorsBeforeInitialization(singleton, beanName);
                        // step 2: afterPropertiesSet
                        // step 3: init-method
                        if (StringUtils.isNotBlank(beanDefinition.getInitMethodName())) {
                            invokeInitMethod(beanDefinition, singleton);
                        }
                        // step 4: postProcessAfterInitialization
                        applyBeanPostProcessorsAfterInitialization(singleton, beanName);
                    }
                }
            }
        }
        return singleton;
    }

    private void invokeInitMethod(BeanDefinition beanDefinition, Object obj) {
        Class<?> clz = beanDefinition.getClass();
        Method method = null;
        try {
            method = clz.getMethod(beanDefinition.getInitMethodName());
            method.invoke(obj);
        } catch (Exception e) {
            throw new CreateBeanInstanceErrorException("invoke init-method error when creating bean, beanName: "
                    + beanDefinition.getId() + ", class: " + beanDefinition.getClassName(), e);
        }
    }

    // 创建 Bean 实例
    private Object createBean(BeanDefinition bd) {
        Class<?> clz = null;
        Object obj = doCreateBean(bd);
        earlySingletonObjects.put(bd.getId(), obj);
        try {
            clz = Class.forName(bd.getClassName());
        } catch (ClassNotFoundException e) {
            throw new CreateBeanInstanceErrorException("class not found when creating bean, beanName: "
                    + bd.getId() + ", class: " + bd.getClassName(), e);
        }
        populateBean(bd, clz, obj);
        earlySingletonObjects.remove(bd.getId());
        return obj;
    }

    private void populateBean(BeanDefinition bd, Class<?> clz, Object obj) {
        handleProperties(bd, clz, obj);
    }

    private void handleProperties(BeanDefinition bd, Class<?> clz, Object obj) {
        log.info("start to handle properties, beanName: {}, class: {}", bd.getId(), bd.getClassName());
        // 处理属性
        PropertyValues propertyValues = bd.getPropertyValues();
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                //对每一个属性，分数据类型分别处理
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String pType = propertyValue.getType();
                String pName = propertyValue.getName();
                Object pValue = propertyValue.getValue();
                boolean isRef = propertyValue.isRef();
                Class<?>[] paramTypes = new Class<?>[1];
                Object[] paramValues = new Object[1];
                if (!isRef) {
                    switch (pType) {
                        case "Integer", "java.lang.Integer" -> paramTypes[0] = Integer.class;
                        case "int" -> paramTypes[0] = int.class;
                        case null, default -> paramTypes[0] = String.class;
                    }
                    paramValues[0] = pValue;
                } else {
                    try {
                        paramTypes[0] = Class.forName(pType);
                    } catch (ClassNotFoundException e) {
                        throw new CreateBeanInstanceErrorException("property class not found when creating bean, beanName: "
                                + bd.getId() + ", class: " + bd.getClassName(), e);
                    }
                    // 递归调用获取依赖的 bean
                    paramValues[0] = getBean((String) pValue);
                }

                //按照setXxxx规范查找setter方法，调用setter方法设置属性
                String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);
                Method method = null;
                try {
                    method = clz.getMethod(methodName, paramTypes);
                    method.invoke(obj, paramValues);
                } catch (Exception e) {
                    throw new CreateBeanInstanceErrorException("set properties error when creating bean, beanName: "
                            + bd.getId() + ", class: " + bd.getClassName(), e);
                }
            }
        }
    }

    private Object doCreateBean(BeanDefinition bd) {
        Class<?> clz = null;
        Object obj = null;
        Constructor<?> con = null;
        try {
            clz = Class.forName(bd.getClassName());
            // 处理构造器参数
            ConstructorArgumentValues constructorArgumentValues = bd.getConstructorArgumentValues();
            //如果有参数
            if (!constructorArgumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[constructorArgumentValues.getArgumentCount()];
                Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];
                //对每一个参数，分数据类型分别处理
                for (int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValue constructorArgumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
                    switch (constructorArgumentValue.getType()) {
                        case "Integer", "java.lang.Integer" -> {
                            paramTypes[i] = Integer.class;
                            paramValues[i] =
                                    Integer.valueOf((String) constructorArgumentValue.getValue());
                        }
                        case "int" -> {
                            paramTypes[i] = int.class;
                            paramValues[i] = Integer.valueOf((String)
                                    constructorArgumentValue.getValue());
                        }
                        case null, default -> {
                            paramTypes[i] = String.class;
                            paramValues[i] = constructorArgumentValue.getValue();
                        }
                    }
                }
                con = clz.getConstructor(paramTypes);
                obj = con.newInstance(paramValues);

            } else { //如果没有参数，直接创建实例
                Constructor<?> constructor = clz.getDeclaredConstructor(); // 获取无参构造函数
                constructor.setAccessible(true); // 如果构造函数是 private，需要设置为可访问
                obj = constructor.newInstance(); // 创建实例
            }
        } catch (Exception e) {
            throw new CreateBeanInstanceErrorException("create bean instance error, beanName: " + bd.getId()
                    + ", class: " + bd.getClassName(), e);
        }
        log.info("create bean instance success, beanName: {}, class: {}", bd.getId(), bd.getClassName());
        return obj;
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
        return beanDefinitionMap.get(name).getClass();
    }


    public void registerBeanDefinition(@NonNull BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanDefinition.getId(), beanDefinition);
    }

    public void refresh() {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            getBean(entry.getKey());
        }
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition bd) {
        this.beanDefinitionMap.put(name, bd);
        if (!bd.isLazyInit()) {
            getBean(name);
        }
    }

    @Override
    public void removeBeanDefinition(String name) {
        this.beanDefinitionMap.remove(name);
        this.removeSingleton(name);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return beanDefinitionMap.get(name);
    }

    @Override
    public boolean containsBeanDefinition(String name) {
        return beanDefinitionMap.containsKey(name);
    }

    abstract public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName);

    abstract public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName);
}
