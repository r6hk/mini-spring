package dev.rennen.jdbc.exception;

/**
 * <br/> 2025/2/3 11:50
 *
 * @author rennen.dev
 */
public class JdbcException extends RuntimeException {
    public JdbcException(String message) {
        super(message);
    }

    public JdbcException(String message, Throwable cause) {
        super(message, cause);
    }
}
