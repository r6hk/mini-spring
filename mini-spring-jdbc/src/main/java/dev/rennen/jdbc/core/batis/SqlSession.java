package dev.rennen.jdbc.core.batis;

import dev.rennen.jdbc.core.JdbcTemplate;
import dev.rennen.jdbc.core.statement.PreparedStatementCallback;

/**
 * <br/> 2025/2/4 17:18
 *
 * @author rennen.dev
 */
public interface SqlSession {
    void setJdbcTemplate(JdbcTemplate jdbcTemplate);
    void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);
    Object selectOne(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback);
}