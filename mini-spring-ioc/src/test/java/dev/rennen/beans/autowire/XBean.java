package dev.rennen.beans.autowire;

import dev.rennen.beans.factory.annotation.Autowired;
import lombok.Data;

/**
 * @author rennen.dev
 * @since 2025/1/4 11:13
 */
@Data
public class XBean {
    @Autowired
    private YBean yBean;
}
