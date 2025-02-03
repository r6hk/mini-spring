package dev.rennen.jdbc.core;

import dev.rennen.jdbc.core.statement.ArgumentPreparedStatementSetter;
import dev.rennen.jdbc.core.statement.PreparedStatementCallback;
import dev.rennen.jdbc.core.statement.StatementCallback;
import dev.rennen.jdbc.exception.JdbcException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;

/**
 * 抽取 JDBC Template 实现基本的 JDBC 访问框架 <br/> 2025/2/3 11:29
 *
 * @author rennen.dev
 */
@Slf4j
@NoArgsConstructor
public class JdbcTemplate {
    private DataSource dataSource;

    // 提供 DataSource 的 Setter，用于 XML 自动装配
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        initializeDatabase(); // 自动初始化数据库
    }

    // 数据库初始化逻辑
    private void initializeDatabase() {
        String initSql = """
                    CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY, name VARCHAR(255));
                    INSERT INTO users (id, name) VALUES
                    (1, 'Alice'),
                    (2, 'Bob'),
                    (3, 'Charlie'),
                    (4, 'Diana');
                """;

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(initSql);
        } catch (SQLException e) {
            log.error("Failed to initialize database", e);
            throw new JdbcException("Failed to initialize database", e);
        }
    }

    // 支持自定义 StatementCallback 的查询
    public <T> T query(StatementCallback<T> statementCallback) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            return statementCallback.doInStatement(stmt);

        } catch (SQLException e) {
            log.error("Failed to execute query", e);
            throw new JdbcException("Failed to execute query", e);
        }
    }

    public <T> T query(String sql, Object[] params, PreparedStatementCallback<T> pstmtCallback) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ArgumentPreparedStatementSetter setter = new ArgumentPreparedStatementSetter(params);
            setter.setValues(pstmt);

            return pstmtCallback.doInStatement(pstmt);

        } catch (SQLException e) {
            log.error("Failed to execute query", e);
            throw new JdbcException("Failed to execute query", e);
        }
    }

}
