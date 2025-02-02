package dev.rennen.webmvc.controller;

import dev.rennen.webmvc.util.annotation.RequestMapping;

/**
 * @author rennen.dev
 * @since 2025/1/5 16:03
 */
public class HelloWorldController {
    @RequestMapping("/test")
    public String doTest() {
        return "hello world for doGet!";
    }
}