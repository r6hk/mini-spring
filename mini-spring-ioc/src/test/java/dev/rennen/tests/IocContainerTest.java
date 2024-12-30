package dev.rennen.tests;

import dev.rennen.beanfactory.ClassPathXmlApplicationContext;
import dev.rennen.exception.BeansException;
import dev.rennen.service.TestService;
import dev.rennen.service.impl.TestServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author rennen.dev
 * @since 2024/12/27 17:30
 */
public class IocContainerTest {

    /**
     * 测试 IoC 容器
     */
    @Test
    public void test1() throws BeansException {
        // 从配置文件中读取 Bean 的定义
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        // 通过getBean()方法获取 Bean 实例
        Object testService = applicationContext.getBean("testService");
        // JUnit 断言检查 Bean 实例是否正确
        assertEquals(TestServiceImpl.class, testService.getClass());
        assertEquals("Hello, Mini Spring!", ((TestService) testService).sayHello());
    }


}
