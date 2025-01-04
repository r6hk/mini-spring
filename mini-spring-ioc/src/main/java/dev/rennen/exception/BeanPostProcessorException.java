package dev.rennen.exception;

/**
 * @author rennen.dev
 * @since 2025/1/4 11:36
 */
public class BeanPostProcessorException extends BeansException {
    // Constructor with a custom message
    public BeanPostProcessorException(String message) {
        super(message);
    }

    // Constructor with a custom message and a cause
    public BeanPostProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with only a cause
    public BeanPostProcessorException(Throwable cause) {
        super(cause);
    }
}
