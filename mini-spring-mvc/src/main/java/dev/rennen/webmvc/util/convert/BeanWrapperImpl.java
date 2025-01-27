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
public class BeanWrapperImpl extends PropertyEditorRegistrySupport {
    Object wrappedObject; //目标对象
    Class<?> clz;
    PropertyValues pvs; //参数值

    public BeanWrapperImpl(Object object) {
        registerDefaultEditors(); //不同数据类型的参数转换器editor
        this.wrappedObject = object;
        this.clz = object.getClass();
    }

    public void setBeanInstance(Object object) {
        this.wrappedObject = object;
    }

    public Object getBeanInstance() {
        return wrappedObject;
    }

    //绑定参数值
    public void setPropertyValues(PropertyValues pvs) {
        this.pvs = pvs;
        for (PropertyValue pv : this.pvs.getPropertyValues()) {
            setPropertyValue(pv);
        }
    }

    //绑定具体某个参数
    public void setPropertyValue(PropertyValue pv) {
        BeanPropertyHandler propertyHandler = new BeanPropertyHandler(pv.getName());
        PropertyEditor pe = this.getCustomEditor(propertyHandler.getPropertyClz());
        if (pe == null) {
            pe = this.getDefaultEditor(propertyHandler.getPropertyClz());
        }

        pe.setAsText((String) pv.getValue());
        propertyHandler.setValue(pe.getValue());
    }


    //一个内部类，用于处理参数，通过getter()和setter()操作属性
    class BeanPropertyHandler {
        Method writeMethod = null;
        Method readMethod = null;
        @Getter
        Class<?> propertyClz = null;

        public BeanPropertyHandler(String propertyName) {
            try {
                //获取参数对应的属性及类型
                Field field = clz.getDeclaredField(propertyName);
                propertyClz = field.getType();
                //获取设置属性的方法，按照约定为setXxxx（）
                this.writeMethod = clz.getDeclaredMethod("set" +
                        propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1), propertyClz);
                //获取读属性的方法，按照约定为getXxxx（）
                this.readMethod = clz.getDeclaredMethod("get" +
                        propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1), propertyClz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //调用getter读属性值
        public Object getValue() {
            Object result = null;
            writeMethod.setAccessible(true);
            try {
                result = readMethod.invoke(wrappedObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        //调用setter设置属性值
        public void setValue(Object value) {
            writeMethod.setAccessible(true);
            try {
                writeMethod.invoke(wrappedObject, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
