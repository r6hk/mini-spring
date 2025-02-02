package dev.rennen.beans.inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PropertyValue 的集合封装形式
 *
 * @author rennen.dev
 * @since 2024/12/30 17:56
 */
public class PropertyValues {

    private final List<PropertyValue> propertyValueList;

    public PropertyValues() {
        this.propertyValueList = new ArrayList<>(0);
    }

    public PropertyValues(Map<String, Object> map) {
        this.propertyValueList = new ArrayList<>(10);
        for (Map.Entry<String, Object> e : map.entrySet()) {
            PropertyValue pv = new PropertyValue(e.getKey(), e.getValue());
            this.propertyValueList.add(pv);
        }
    }

    public List<PropertyValue> getPropertyValueList() {
        return this.propertyValueList;
    }

    public int size() {
        return this.propertyValueList.size();
    }

    public void addPropertyValue(PropertyValue pv) {
        this.propertyValueList.add(pv);
    }

    public void addPropertyValue(String propertyType, String propertyName, Object propertyValue, boolean isRef) {
        addPropertyValue(new PropertyValue(propertyType, propertyName, propertyValue, isRef));
    }

    public void removePropertyValue(PropertyValue pv) {
        this.propertyValueList.remove(pv);
    }

    public void removePropertyValue(String propertyName) {
        this.propertyValueList.remove(getPropertyValue(propertyName));
    }

    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[0]);
    }

    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue pv : this.propertyValueList) {
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

    public Object get(String propertyName) {
        PropertyValue pv = getPropertyValue(propertyName);
        return pv != null ? pv.getValue() : null;
    }

    public boolean contains(String propertyName) {
        return getPropertyValue(propertyName) != null;
    }

    public boolean isEmpty() {
        return this.propertyValueList.isEmpty();
    }
}
