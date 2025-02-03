package dev.rennen.jdbc.core.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 把 JDBC 返回的 ResultSet 里的某一行数据映射成一个对象
 * <br/> 2025/2/3 21:10
 *
 * @author rennen.dev
 */
@FunctionalInterface
public interface RowMapper<T> {
    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
