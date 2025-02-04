package dev.rennen.jdbc.core.batis;

import dev.rennen.jdbc.core.JdbcTemplate;
import dev.rennen.jdbc.core.statement.PreparedStatementCallback;
import lombok.Getter;
import lombok.Setter;

/**
 * <br/> 2025/2/4 17:18
 *
 * @author rennen.dev
 */
@Getter @Setter
public class DefaultSqlSession implements SqlSession {
    JdbcTemplate jdbcTemplate;

    SqlSessionFactory sqlSessionFactory;

    @Override
    public Object selectOne(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback) {
        String sql = this.sqlSessionFactory.getMapperNode(sqlid).getSql();
        return jdbcTemplate.query(sql, args, pstmtcallback);
    }

    private void buildParameter() {
    }

    private Object resultSet2Obj() {
        return null;
    }
}
