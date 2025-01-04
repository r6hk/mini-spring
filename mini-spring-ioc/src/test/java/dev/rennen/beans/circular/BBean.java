package dev.rennen.beans.circular;

import lombok.Data;

/**
 * @author rennen.dev
 * @since 2025/1/4 10:59
 */
@Data
public class BBean {
    private CBean cBean;

    public String sayHello() {
        return "Hello from YBean!";
    }
}
