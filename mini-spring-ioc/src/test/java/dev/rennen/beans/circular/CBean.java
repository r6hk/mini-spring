package dev.rennen.beans.circular;

import lombok.Data;

/**
 * @author rennen.dev
 * @since 2025/1/4 10:59
 */
@Data
public class CBean {
    private ABean aBean;

    public String sayHello() {
        return "Hello from CBean!";
    }
}
