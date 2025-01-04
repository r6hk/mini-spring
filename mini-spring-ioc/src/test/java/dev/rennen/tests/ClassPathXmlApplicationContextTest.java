package dev.rennen.tests;

import dev.rennen.beans.autowire.XBean;
import dev.rennen.beans.circular.ABean;
import dev.rennen.beans.circular.BBean;
import dev.rennen.beans.circular.CBean;
import dev.rennen.beans.factory.ClassPathXmlApplicationContext;
import dev.rennen.beans.impl.TestDaoImpl;
import dev.rennen.beans.impl.TestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClassPathXmlApplicationContextTest {

    private ClassPathXmlApplicationContext context;

    @BeforeEach
    public void setUp() {
        context = new ClassPathXmlApplicationContext("beans.xml");
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
        assertTrue(context.containsBean("testService"), "上下文应该包含名为 'testService' 的 Bean");
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

    /**
     * 测试三种类型的注入，包括 int、String 和引用类型
     */
    @Test
    public void testSetter() {
        try {
            TestServiceImpl testService = (TestServiceImpl) context.getBean("testService");
            assertNotNull(testService, "从上下文中获取的 Bean 不应该为空");
            assertTrue(context.containsBean("testService"), "上下文应该包含名为 'testService' 的 Bean");
            assertEquals("rennen", testService.getName(), "testService 的 getName 方法应该返回 'rennen'");
            assertEquals(18, testService.getAge(), "testService 的 getAge 方法应该返回 18");
            assertEquals("Hello from DAO!", testService.getTestDao().sayHello(), "testService 的 getDao 方法应该返回 'Hello from DAO!'");
        } catch (Exception e) {
            fail("测试 set 方法注入时发生错误: " + e.getMessage());
        }
    }

    /**
     * 测试构造器注入, 暂不支持引用数据类型
     */
    @Test
    public void testConstruct() {
        try {
            TestDaoImpl testDao = (TestDaoImpl) context.getBean("testDao");
            assertNotNull(testDao, "从上下文中获取的 Bean 不应该为空");
            assertTrue(context.containsBean("testDao"), "上下文应该包含名为 'testDao' 的 Bean");
            assertEquals("abc", testDao.getName(), "testDao 的 getName 方法应该返回 'abc'");
            assertEquals(3, testDao.getAge(), "testDao 的 getAge 方法应该返回 3");
        } catch (Exception e) {
            fail("测试构造器注入时发生错误: " + e.getMessage());
        }
    }

    /**
     * 测试三个 Bean 的循环依赖
     */
    @Test
    public void testCircularDependency() {
        try {
            ABean aBean = (ABean) context.getBean("aBean");
            assertNotNull(aBean, "从上下文中获取的 Bean 不应该为空");
            assertNotNull(aBean.getBBean(), "aBean 的 bBean 属性不应该为空");
            assertEquals("Hello from YBean!", aBean.getBBean().sayHello(), "aBean 的 bBean 属性应该返回 'Hello from YBean!'");

            BBean bBean = (BBean) context.getBean("bBean");
            assertNotNull(bBean, "从上下文中获取的 Bean 不应该为空");
            assertNotNull(bBean.getCBean(), "bBean 的 cBean 属性不应该为空");
            assertEquals("Hello from CBean!", bBean.getCBean().sayHello(), "bBean 的 cBean 属性应该返回 'Hello from CBean!'");

            CBean cBean = (CBean) context.getBean("cBean");
            assertNotNull(cBean, "从上下文中获取的 Bean 不应该为空");
            assertNotNull(cBean.getABean(), "cBean 的 aBean 属性不应该为空");
            assertEquals("Hello from XBean!", cBean.getABean().sayHello(), "cBean 的 aBean 属性应该返回 'Hello from XBean!'");
        } catch (Exception e) {
            fail("测试循环依赖时发生错误", e);
        }
    }

    /**
     * 测试 @Autowired 注入
     */
    @Test
    public void testAutowired() {
        try {
            XBean xBean = (XBean) context.getBean("xBean");
            assertNotNull(xBean, "从上下文中获取的 Bean 不应该为空");
            assertNotNull(xBean.getYBean(), "xBean 的 yBean 属性不应该为空");
            assertEquals("Hello from YBean!", xBean.getYBean().sayHello(), "xBean 的 yBean 属性应该返回 'Hello from YBean!'");
        } catch (Exception e) {
            fail("测试 @Autowired 注入时发生错误: ", e);
        }
    }

}
