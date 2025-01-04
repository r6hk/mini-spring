package dev.rennen.beans.impl;

import dev.rennen.beans.TestDao;
import dev.rennen.beans.TestService;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rennen.dev
 * @since 2024/12/27 17:35
 */
@Setter
@Getter
public class TestServiceImpl implements TestService {
    private String name;
    private int age;
    private TestDao testDao;

    @Override
    public String sayHello() {
        return "Hello, Mini Spring!";
    }
}
