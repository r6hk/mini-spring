package dev.rennen.jdbc.core;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * <br/> 2025/2/3 16:23
 *
 * @author rennen.dev
 */
@FunctionalInterface
public interface StatementCallback<T> {
    T doInStatement(Statement stmt) throws SQLException;
}