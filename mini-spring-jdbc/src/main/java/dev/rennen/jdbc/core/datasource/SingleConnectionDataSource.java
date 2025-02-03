package dev.rennen.jdbc.core.datasource;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * <br/> 2025/2/3 16:44
 *
 * @author rennen.dev
 */
@Getter
@Setter
public class SingleConnectionDataSource extends AbstractDataSource {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Properties connectionProperties;
    private Connection connection;

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        try {
            Class.forName(this.driverClassName);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Could not load JDBC driver class [" + driverClassName + "]", ex);
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
        if (connectionProperties != null) {
            mergedProps.putAll(connectionProperties);
        }
        if (username != null) {
            mergedProps.setProperty("user", username);
        }
        if (password != null) {
            mergedProps.setProperty("password", password);
        }
        this.connection = DriverManager.getConnection(url, mergedProps);
        return this.connection;
    }
}