package dev.rennen.jdbc.core.pool;

import dev.rennen.jdbc.core.datasource.PooledDataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <br/> 2025/2/3 22:31
 *
 * @author rennen.dev
 */
@Slf4j
public class PooledConnection extends AbstractConnection {
    @Getter
    private final Connection realConnection;
    private final PooledDataSource dataSource;

    public PooledConnection(Connection realConnection, PooledDataSource dataSource) {
        this.realConnection = realConnection;
        this.dataSource = dataSource;
    }

    public boolean isValid() {
        try {
            return !realConnection.isClosed() && realConnection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void close() throws SQLException {
        if (isValid()) {
            dataSource.releaseConnection(this);
        } else {
            dataSource.closeQuietly(realConnection);
        }
    }

    // 代理所有Connection方法到realConnection
    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return realConnection.prepareStatement(sql);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return realConnection.createStatement();
    }
}