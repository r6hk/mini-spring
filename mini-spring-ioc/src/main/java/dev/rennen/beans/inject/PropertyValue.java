package dev.rennen.beans.inject;

import lombok.Getter;

/**
 * @author rennen.dev
 * @since 2024/12/30 17:49
 */

@Getter
public class PropertyValue {

    private final String name;
    private final Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}
