package dev.rennen.jdbc.test.service;

import dev.rennen.beans.factory.annotation.Autowired;
import dev.rennen.jdbc.core.JdbcTemplate;
import dev.rennen.jdbc.test.entity.User;

import java.sql.ResultSet;

/**
 * <br/> 2025/2/3 11:59
 *
 * @author rennen.dev
 */
public class UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public User getUserInfoFunctional(int userid) {
        String sql = "select * from users where id=" + userid;

        return jdbcTemplate.query((stmt)->{
            ResultSet rs = stmt.executeQuery(sql);
            User rtnUser = null;
            if (rs.next()) {
                rtnUser = new User();
                rtnUser.setId(userid);
                rtnUser.setName(rs.getString("name"));
            }
            return rtnUser;
        });
    }

}
