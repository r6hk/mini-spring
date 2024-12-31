package dev.rennen.beanfactory;

import dev.rennen.beans.define.BeanDefinition;
import dev.rennen.beans.inject.ArgumentValue;
import dev.rennen.beans.inject.ArgumentValues;
import dev.rennen.beans.inject.PropertyValue;
import dev.rennen.beans.inject.PropertyValues;
import dev.rennen.exception.CreateBeanInstanceErrorException;
import dev.rennen.exception.NoSuchBeanDefinitionException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author rennen.dev
 * @since 2024/12/27 17:55
 */
@Slf4j
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();


    //getBean，容器的核心方法
    @Override
    public Object getBean(@NonNull String beanName) {
        //先尝试直接拿Bean实例
        Object singleton = this.getSingleton(beanName);
        //如果此时还没有这个Bean的实例，则获取它的定义来创建实例
        if (singleton == null) {
            synchronized (this) {
                // 第二次检查，加锁后确保不会重复创建
                singleton = this.getSingleton(beanName);
                if (singleton == null) {
                    //获取Bean的定义
                    BeanDefinition beanDefinition = Optional.ofNullable(beanDefinitionMap.get(beanName))
                            .orElseThrow(NoSuchBeanDefinitionException::new);
                    singleton = createBean(beanDefinition);
                    registerSingleton(beanName, singleton);
                }
            }
        }
        return singleton;
    }

    private Object createBean(BeanDefinition bd) {
        Class<?> clz = null;
        Object obj = null;
        Constructor<?> con = null;
        try {
            clz = Class.forName(bd.getClassName());
            // 处理构造器参数
            ArgumentValues argumentValues = bd.getConstructorArgumentValues();
            //如果有参数
            if (!argumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
                Object[] paramValues = new Object[argumentValues.getArgumentCount()];
                //对每一个参数，分数据类型分别处理
                for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                    ArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
                    switch (argumentValue.getType()) {
                        case "Integer", "java.lang.Integer" -> {
                            paramTypes[i] = Integer.class;
                            paramValues[i] =
                                    Integer.valueOf((String) argumentValue.getValue());
                        }
                        case "int" -> {
                            paramTypes[i] = int.class;
                            paramValues[i] = Integer.valueOf((String)
                                    argumentValue.getValue());
                        }
                        case null, default -> {
                            paramTypes[i] = String.class;
                            paramValues[i] = argumentValue.getValue();
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
        handleProperties(bd, clz, obj);
        return obj;
    }

    private void handleProperties(BeanDefinition bd, Class<?> clz, Object obj) {
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
