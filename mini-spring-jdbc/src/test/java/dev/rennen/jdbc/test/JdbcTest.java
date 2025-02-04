package dev.rennen.jdbc.test;

import dev.rennen.beans.factory.ClassPathXmlApplicationContext;
import dev.rennen.jdbc.test.entity.User;
import dev.rennen.jdbc.test.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    public void test1() {
        UserService userService = (UserService) context.getBean("userService");
        User userInfo = userService.getUserInfoFunctional(1);
        assertNotNull(userInfo);
        assertEquals(1, userInfo.getId());
        assertEquals("Alice", userInfo.getName());
    }

    @Test
    public void test2() {
        UserService userService = (UserService) context.getBean("userService");
        User userInfo = userService.getUserInfoPrepared(1);
        assertNotNull(userInfo);
        assertEquals(1, userInfo.getId());
        assertEquals("Alice", userInfo.getName());
    }

    @Test
    public void test3() {
        UserService userService = (UserService) context.getBean("userService");
        List<User> userInfos = userService.getUsersByIds(List.of(1, 2, 3, 4));
        assertEquals(4, userInfos.size());

        User user1 = userInfos.get(0);
        assertEquals(1, user1.getId());
        assertEquals("Alice", user1.getName());

        User user2 = userInfos.get(1);
        assertEquals(2, user2.getId());
        assertEquals("Bob", user2.getName());

        User user3 = userInfos.get(2);
        assertEquals(3, user3.getId());
        assertEquals("Charlie", user3.getName());

        User user4 = userInfos.get(3);
        assertEquals(4, user4.getId());
        assertEquals("Diana", user4.getName());
    }

    @Test
    public void test4() {
        UserService userService = (UserService) context.getBean("userService");
        User userInfo = userService.getUserUsingBatis(1);
        assertNotNull(userInfo);
        assertEquals(1, userInfo.getId());
        assertEquals("Alice", userInfo.getName());
    }
}
