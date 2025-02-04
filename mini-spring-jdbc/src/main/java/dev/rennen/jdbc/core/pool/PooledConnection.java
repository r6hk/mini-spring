package dev.rennen.jdbc.core.pool;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <br/> 2025/2/3 22:31
 *
 * @author rennen.dev
 */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PooledConnection extends AbstractConnection {
    private Connection connection;
    private boolean active;

    @Override
    public void close() {
        this.active = false;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return this.connection.prepareStatement(sql);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }
}
