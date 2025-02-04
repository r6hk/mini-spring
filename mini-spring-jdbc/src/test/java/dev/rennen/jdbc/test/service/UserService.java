package dev.rennen.jdbc.test.service;

import dev.rennen.beans.factory.annotation.Autowired;
import dev.rennen.jdbc.core.JdbcTemplate;
import dev.rennen.jdbc.core.batis.SqlSessionFactory;
import dev.rennen.jdbc.test.entity.User;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <br/> 2025/2/3 11:59
 *
 * @author rennen.dev
 */
public class UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    public User getUserInfoFunctional(int userid) {
        String sql = "select * from users where id=" + userid;

        return jdbcTemplate.query((stmt) -> {
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

    /**
     * 使用 PreparedStatement 查询用户信息，支持参数绑定，防止 SQL 注入
     *
     * @param userId
     * @return
     */
    public User getUserInfoPrepared(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";

        return jdbcTemplate.query(sql, new Object[]{userId}, pstmt -> {
            ResultSet rs = pstmt.executeQuery();
            User user = null;
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
            }
            return user;
        });
    }

    /**
     * 批量查询用户信息，支持参数绑定，防止 SQL 注入
     *
     * @param userIds 用户 ID 列表
     * @return 用户列表
     */
    public List<User> getUsersByIds(List<Integer> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 动态构建 IN 子句的占位符 (?, ?, ?, ...)
        String placeholders = userIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));
        String sql = "SELECT * FROM users WHERE id IN (" + placeholders + ")";

        // 将 List<Integer> 转换为 Object[] 以兼容参数绑定
        Object[] params = userIds.toArray();

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            return user;
        });
    }

    public User getUserUsingBatis(int userId) {
        return (User) sqlSessionFactory.openSession().selectOne("com.test.entity.User.getUserInfo",
                new Object[]{userId},
                pstmt -> {
                    ResultSet rs = pstmt.executeQuery();
                    User user = null;
                    if (rs.next()) {
                        user = new User();
                        user.setId(rs.getInt("id"));
                        user.setName(rs.getString("name"));
                    }
                    return user;
                });
    }
}
