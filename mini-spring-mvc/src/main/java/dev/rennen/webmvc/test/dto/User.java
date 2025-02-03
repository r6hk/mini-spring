package dev.rennen.webmvc.test.dto;

import lombok.Data;

import java.util.Date;

/**
 * 2025/2/2 20:23
 *
 * @author rennen.dev
 */
@Data
public class User {
    int id = 1;

    String name = "";

    Date birthday = new Date();

}
