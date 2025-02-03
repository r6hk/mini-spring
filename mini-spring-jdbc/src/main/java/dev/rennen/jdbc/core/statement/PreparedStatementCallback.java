package dev.rennen.jdbc.core.statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <br/> 2025/2/3 20:55
 *
 * @author rennen.dev
 */
@FunctionalInterface
public interface PreparedStatementCallback<T> {
    T doInStatement(PreparedStatement stmt) throws SQLException;
}