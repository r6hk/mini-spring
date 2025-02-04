package dev.rennen.tests;

import dev.rennen.beans.AopService;
import dev.rennen.beans.factory.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <br/> 2025/2/4 22:42
 *
 * @author rennen.dev
 */
class AopProxyTest {
    private ClassPathXmlApplicationContext context;

    @BeforeEach
    public void setUp() {
        context = new ClassPathXmlApplicationContext("beans.xml");
    }

    @Test
    void getProxy() {
        Object action = context.getBean("action");
        assertInstanceOf(AopService.class, action);
        boolean opposite = ((AopService) action).doAction(true);
        assertFalse(opposite);
    }
}