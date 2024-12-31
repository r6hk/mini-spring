package dev.rennen.beans.inject;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用于处理属性注入
 *
 * @author rennen.dev
 * @since 2024/12/30 17:49
 */

@Getter
@AllArgsConstructor
public class PropertyValue {

    private final String type;
    private final String name;
    private final Object value;
    private final boolean isRef;

}
