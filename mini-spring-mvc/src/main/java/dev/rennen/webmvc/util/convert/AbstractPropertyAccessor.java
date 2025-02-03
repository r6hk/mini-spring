package dev.rennen.webmvc.util.convert;

import dev.rennen.beans.inject.PropertyValue;
import dev.rennen.beans.inject.PropertyValues;

/**
 * 2025/2/3 10:14
 *
 * @author rennen.dev
 */
public abstract class AbstractPropertyAccessor extends PropertyEditorRegistrySupport{

    PropertyValues pvs;

    public AbstractPropertyAccessor() {
        super();

    }


    public void setPropertyValues(PropertyValues pvs) {
        this.pvs = pvs;
        for (PropertyValue pv : this.pvs.getPropertyValues()) {
            setPropertyValue(pv);
        }
    }

    public abstract void setPropertyValue(PropertyValue pv) ;

}
