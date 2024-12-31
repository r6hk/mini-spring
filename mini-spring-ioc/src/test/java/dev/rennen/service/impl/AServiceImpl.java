package dev.rennen.service.impl;

import dev.rennen.service.AService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rennen.dev
 * @since 2024/12/31 16:26
 */
@Getter
@Setter
public class AServiceImpl implements AService {
    private String name;
    private int level;
    private String property1;
    private String property2;

    public AServiceImpl(String name, int level) {
        this.name = name;
        this.level = level;
        System.out.println(this.name + "," + this.level);
    }

    @Override
    public void sayHello() {
        System.out.println(this.property1 + "," + this.property2);
    }
}