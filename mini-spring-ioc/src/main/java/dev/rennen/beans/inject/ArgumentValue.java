package dev.rennen.beans.inject;

import lombok.Data;

/**
 * 用于处理构造器注入
 *
 * @author rennen.dev
 * @since 2024/12/30 17:46
 */
@Data
public class ArgumentValue {

    private Object value;
    private String type;
    private String name;

    public ArgumentValue(Object value, String type, String name) {
        this.value = value;
        this.type = type;
        this.name = name;
    }

    public ArgumentValue(Object value, String type) {
        this.value = value;
        this.type = type;
    }
}
