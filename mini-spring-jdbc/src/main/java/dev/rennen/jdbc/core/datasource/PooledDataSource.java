package dev.rennen.jdbc.core.datasource;

import dev.rennen.jdbc.core.pool.PooledConnection;
import dev.rennen.jdbc.exception.JdbcException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 池化形式的数据源，用于管理数据库连接池
 * <br/>
 * 2025/2/3 22:39
 *
 * @author rennen.dev
 */
@Getter @Setter
@Slf4j
public class PooledDataSource extends AbstractDataSource {
    private List<PooledConnection> connections = null;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int initialSize = 2;
    private Properties connectionProperties;

    private void initPool() {
        this.connections = new ArrayList<>(initialSize);
        try {
            for (int i = 0; i < initialSize; i++) {
                Connection connect = DriverManager.getConnection(url, username, password);
                PooledConnection pooledConnection = new PooledConnection(connect, false);
                this.connections.add(pooledConnection);
            }
            log.info("Connection pool initialized with {} connections", initialSize);
        } catch (Exception e) {
            log.error("Failed to initialize connection pool", e);
            throw new JdbcException("Failed to initialize connection pool", e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnectionFromDriver(getUsername(), getPassword());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnectionFromDriver(username, password);
    }

    protected Connection getConnectionFromDriver(String username, String password) throws SQLException {
        Properties mergedProps = new Properties();
        Properties connProps = getConnectionProperties();
        if (connProps != null) {
            mergedProps.putAll(connProps);
        }
        if (username != null) {
            mergedProps.setProperty("user", username);
        }
        if (password != null) {
            mergedProps.setProperty("password", password);
        }

        if (this.connections == null) {
            initPool();
        }

        PooledConnection pooledConnection = getAvailableConnection();

        while (pooledConnection == null) {
            pooledConnection = getAvailableConnection();

            if (pooledConnection == null) {
                try {
                    TimeUnit.MILLISECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return pooledConnection;
    }

    private PooledConnection getAvailableConnection() throws SQLException {
        for (PooledConnection pooledConnection : this.connections) {
            if (!pooledConnection.isActive()) {
                pooledConnection.setActive(true);
                return pooledConnection;
            }
        }

        return null;
    }

    protected Connection getConnectionFromDriverManager(String url, Properties props) throws SQLException {
        return DriverManager.getConnection(url, props);
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        try {
            Class.forName(this.driverClassName);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Could not load JDBC driver class [" + driverClassName + "]", ex);
        }

    }
}
