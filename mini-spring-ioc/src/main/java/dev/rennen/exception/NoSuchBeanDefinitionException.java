package dev.rennen.exception;

/**
 * @author rennen.dev
 * @since 2024/12/30 18:17
 */
public class NoSuchBeanDefinitionException extends BeansException {


    // Constructor with a default message
    public NoSuchBeanDefinitionException() {
        super("No such bean definition found.");
    }

    // Constructor with a custom message
    public NoSuchBeanDefinitionException(String message) {
        super(message);
    }

    // Constructor with a custom message and a cause
    public NoSuchBeanDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with only a cause
    public NoSuchBeanDefinitionException(Throwable cause) {
        super(cause);
    }

}
