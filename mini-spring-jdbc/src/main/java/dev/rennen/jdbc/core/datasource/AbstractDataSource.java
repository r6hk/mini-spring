package dev.rennen.jdbc.core.datasource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * <br/>
 * 2025/2/3 16:55
 * @author rennen.dev
 */
public abstract class AbstractDataSource implements DataSource {

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null; // 提供默认实现
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        // 默认不做处理
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        // 默认不做处理
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0; // 默认返回0
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("getParentLogger not supported.");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return iface.cast(this);
        }
        throw new SQLException("No wrapper for " + iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }

    // 保留 getConnection 为抽象方法，具体子类实现
    @Override
    public abstract Connection getConnection() throws SQLException;

    @Override
    public abstract Connection getConnection(String username, String password) throws SQLException;
}
