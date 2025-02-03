package dev.rennen.jdbc.test;

import dev.rennen.beans.factory.ClassPathXmlApplicationContext;
import dev.rennen.jdbc.test.entity.User;
import dev.rennen.jdbc.test.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * <br/> 2025/2/3 12:05
 *
 * @author rennen.dev
 */
public class JdbcTest {

    private ClassPathXmlApplicationContext context;

    @BeforeEach
    public void setUp() {
        context = new ClassPathXmlApplicationContext("beans.xml");
    }

    @Test
    public void test() {
        UserService userService = (UserService) context.getBean("userService");
        User userInfo = userService.getUserInfoFunctional(1);
        System.out.println(userInfo);
    }
}
