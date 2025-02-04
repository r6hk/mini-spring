package dev.rennen.jdbc.core.datasource;

import dev.rennen.jdbc.core.pool.PooledConnection;
import dev.rennen.jdbc.exception.JdbcException;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 池化形式的数据源，用于管理数据库连接池
 * <br/>
 * 2025/2/3 22:39
 *
 * @author rennen.dev
 */
@Slf4j
public class PooledDataSource extends AbstractDataSource {
    // 阻塞队列存储空闲连接
    private final BlockingQueue<PooledConnection> idleConnections;
    // 当前活跃连接数量
    private final AtomicInteger activeCount = new AtomicInteger(0);
    private final String url;
    private final String username;
    private final String password;
    // 最大连接数
    private final int maxTotal;
    // 是否已初始化
    private volatile boolean initialized;
    // 初始化锁
    private final Object initLock = new Object();

    public PooledDataSource(String driverClassName, String url, String username,
                            String password, int maxTotal) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.maxTotal = maxTotal;
        this.idleConnections = new LinkedBlockingQueue<>(maxTotal);

        initializeDriver(driverClassName);
        initializePool();
    }

    private void initializeDriver(String driverClassName) {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new JdbcException("Failed to load JDBC driver", e);
        }
    }

    private void initializePool() {
        synchronized (initLock) {
            if (!initialized) {
                for (int i = 0; i < Math.min(maxTotal, 2); i++) {
                    try {
                        Connection conn = createConnection();
                        idleConnections.offer(new PooledConnection(conn, this));
                        activeCount.incrementAndGet();
                    } catch (SQLException e) {
                        log.error("Failed to initialize connection pool", e);
                    }
                }
                initialized = true;
                log.info("Connection pool initialized");
            }
        }
    }

    private Connection createConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        return DriverManager.getConnection(url, props);
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (!initialized) initializePool();

        try {
            PooledConnection conn = idleConnections.poll(30, TimeUnit.MILLISECONDS);
            if (conn != null) return conn;

            while (true) {
                int currentCount = activeCount.get();
                if (currentCount >= maxTotal) {
                    break; // 达到上限，退出循环
                }
                if (activeCount.compareAndSet(currentCount, currentCount + 1)) {
                    try {
                        return new PooledConnection(createConnection(), this);
                        // 该连接在释放的时候会重新放回 idleConnections
                    } catch (SQLException e) {
                        activeCount.decrementAndGet();
                        throw e;
                    }
                }
            }
            throw new SQLException("Connection pool exhausted");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Interrupted while acquiring connection", e);
        }
    }

    public void releaseConnection(PooledConnection connection) {
        if (connection.isValid()) {
            if (idleConnections.offer(connection)) {
                return;
            }
            closeQuietly(connection.getRealConnection());
            activeCount.decrementAndGet();
        } else {
            closeQuietly(connection.getRealConnection());
            activeCount.decrementAndGet();
        }
    }

    public void closeQuietly(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            log.warn("Error closing connection", e);
        }
    }

    @Override
    public Connection getConnection(String username, String password) {
        throw new UnsupportedOperationException("This datasource uses fixed credentials");
    }
}