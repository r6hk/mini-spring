package dev.rennen.webmvc.util.convert;

import dev.rennen.beans.inject.PropertyValue;
import dev.rennen.beans.inject.PropertyValues;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 2025/1/26 10:57
 *
 * @author rennen.dev
 */
public class BeanWrapperImpl extends AbstractPropertyAccessor {
    Object wrappedObject; // 目标对象
    Class<?> clz;
    PropertyValues pvs;   // 参数值

    public BeanWrapperImpl(Object object) {
        registerDefaultEditors(); // 不同数据类型的参数转换器 editor
        this.wrappedObject = object;
        this.clz = object.getClass();
    }

    public void setBeanInstance(Object object) {
        this.wrappedObject = object;
    }

    public Object getBeanInstance() {
        return wrappedObject;
    }

    // 绑定参数值
    public void setPropertyValues(PropertyValues pvs) {
        this.pvs = pvs;
        for (PropertyValue pv : this.pvs.getPropertyValues()) {
            setPropertyValue(pv);
        }
    }

    // 绑定具体某个参数
    public void setPropertyValue(PropertyValue pv) {
        BeanPropertyHandler propertyHandler = new BeanPropertyHandler(pv.getName());
        PropertyEditor pe = this.getCustomEditor(propertyHandler.getPropertyClz());
        if (pe == null) {
            pe = this.getDefaultEditor(propertyHandler.getPropertyClz());
        }

        pe.setAsText((String) pv.getValue());
        propertyHandler.setValue(pe.getValue());
    }

    // 内部类：用于处理属性，通过 getter() 和 setter() 操作属性
    class BeanPropertyHandler {
        Method writeMethod = null;
        Method readMethod = null;
        @Getter
        Class<?> propertyClz = null;

        public BeanPropertyHandler(String propertyName) {
            try {
                // 获取属性及其类型
                Field field = clz.getDeclaredField(propertyName);
                propertyClz = field.getType();

                // 获取 setter 方法（例如：setXxx）
                this.writeMethod = clz.getDeclaredMethod(
                        "set" + capitalize(propertyName), propertyClz
                );
                writeMethod.setAccessible(true); // 确保私有方法可访问

                // 获取 getter 方法（例如：getXxx），不带参数
                this.readMethod = clz.getDeclaredMethod(
                        "get" + capitalize(propertyName)
                );
                readMethod.setAccessible(true);  // 确保私有方法可访问
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 调用 getter 读取属性值
        public Object getValue() {
            try {
                return readMethod.invoke(wrappedObject);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // 调用 setter 设置属性值
        public void setValue(Object value) {
            try {
                writeMethod.invoke(wrappedObject, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 工具方法：首字母大写
        private String capitalize(String str) {
            if (str == null || str.isEmpty()) return str;
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }
}
