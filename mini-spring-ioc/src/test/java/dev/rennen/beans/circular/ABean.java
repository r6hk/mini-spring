package dev.rennen.beans.circular;

import lombok.Data;

/**
 * @author rennen.dev
 * @since 2025/1/4 10:59
 */
@Data
public class ABean {
    private BBean bBean;

    public String sayHello() {
        return "Hello from XBean!";
    }
}
