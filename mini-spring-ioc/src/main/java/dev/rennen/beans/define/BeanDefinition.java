package dev.rennen.beans.define;

import dev.rennen.beans.inject.ArgumentValues;
import dev.rennen.beans.inject.PropertyValues;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author rennen.dev
 * @since 2024/12/27 16:44
 */
@Data
public class BeanDefinition {

    public static final String SCOPE_SINGLETON = "singleton";
    public static final String SCOPE_PROTOTYPE = "prototype";

    private boolean lazyInit = false;
    private String[] dependsOn;
    private ArgumentValues constructorArgumentValues;
    private PropertyValues propertyValues;
    private String initMethodName;
    private volatile Object beanClass;
    private String id;
    private String className;
    private String scope = SCOPE_SINGLETON;

    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }
}
