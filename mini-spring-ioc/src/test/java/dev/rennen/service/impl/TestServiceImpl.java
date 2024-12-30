package dev.rennen.service.impl;

import dev.rennen.service.TestService;

/**
 * @author rennen.dev
 * @since 2024/12/27 17:35
 */
public class TestServiceImpl implements TestService {
    @Override
    public String sayHello() {
        return "Hello, Mini Spring!";
    }
}
