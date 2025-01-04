package dev.rennen.beans.impl;

import dev.rennen.beans.TestDao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rennen.dev
 * @since 2025/1/3 18:08
 */
@AllArgsConstructor
@Getter
@Setter
public class TestDaoImpl implements TestDao {
    private String name;

    private int age;

    @Override
    public String sayHello() {
        return "Hello from DAO!";
    }
}
