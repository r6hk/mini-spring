package dev.rennen.tests;

import dev.rennen.beans.factory.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClassPathXmlApplicationContextTest {

    private ClassPathXmlApplicationContext context;

    @BeforeEach
    public void setUp() {
        try {
            // 假设 test-config.xml 是一个有效的配置文件，路径需要实际存在
            context = new ClassPathXmlApplicationContext("beans.xml");
        } catch (Exception e) {
            fail("初始化 ClassPathXmlApplicationContext 失败: " + e.getMessage());
        }
    }

    @Test
    public void testGetBean() {
        try {
            Object testService = context.getBean("testService");
            assertNotNull(testService, "从上下文中获取的 Bean 不应该为空");
            assertTrue(context.containsBean("testService"), "上下文应该包含名为 'testService' 的 Bean");
        } catch (Exception e) {
            fail("getBean 方法执行失败: " + e.getMessage());
        }
    }

    @Test
    public void testContainsBean() {
        // TODO: 容器初始化时不会创建 Bean 实例，需要实现容器的 refresh() 方法，将 Bean 实例化
        assertFalse(context.containsBean("testService"), "上下文应该包含名为 'testService' 的 Bean");
        assertFalse(context.containsBean("nonExistentBean"), "上下文不应该包含名为 'nonExistentBean' 的 Bean");
    }

    @Test
    public void testRegisterBean() {
        String beanName = "newBean";
        Object newBean = new Object();
        
        context.registerBean(beanName, newBean);
        assertTrue(context.containsBean(beanName), "上下文应该包含新注册的 Bean");
        assertEquals(newBean, context.getBean(beanName), "获取的 Bean 应该与注册时的对象相同");

    }

    @Test
    public void testSingletonScope() {
        try {
            Object bean1 = context.getBean("testService");
            Object bean2 = context.getBean("testService");
            assertNotNull(bean1, "从上下文中获取的 singletonBean 不应该为空");
            assertSame(bean1, bean2, "singletonBean 应该是单例的，获取的对象引用应相同");
        } catch (Exception e) {
            fail("测试单例作用域时发生错误: " + e.getMessage());
        }
    }
}
