package dev.rennen.tests;

import dev.rennen.beans.AopService;
import dev.rennen.beans.factory.ClassPathXmlApplicationContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <br/> 2025/2/4 22:42
 *
 * @author rennen.dev
 */
@Slf4j
class AopProxyTest {
    private ClassPathXmlApplicationContext context;

    @BeforeEach
    public void setUp() {
        context = new ClassPathXmlApplicationContext("beans.xml");
    }

    @Test
    void test1() {
        Object action = context.getBean("action");
        assertInstanceOf(AopService.class, action);
        boolean opposite = ((AopService) action).doAction(true);
        assertTrue(opposite);
    }

    @Test
    void test2() {
        Object action = context.getBean("action");
        boolean opposite = ((AopService) action).doAction(true);
        log.info("opposite: {}", opposite);
    }
}