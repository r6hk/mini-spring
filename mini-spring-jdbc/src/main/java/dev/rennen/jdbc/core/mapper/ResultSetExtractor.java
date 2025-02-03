package dev.rennen.jdbc.core.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 把 JDBC 返回的 ResultSet 数据集映射为一个集合对象
 * <br/> 2025/2/3 21:11
 *
 * @author rennen.dev
 */
@FunctionalInterface
public interface ResultSetExtractor<T> {
    T extractData(ResultSet rs) throws SQLException;
}

