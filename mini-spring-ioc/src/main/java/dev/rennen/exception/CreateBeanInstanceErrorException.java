package dev.rennen.exception;

/**
 * @author rennen.dev
 * @since 2024/12/31 17:03
 */
public class CreateBeanInstanceErrorException extends BeansException {    // Constructor with a default message
    public CreateBeanInstanceErrorException() {
        super("No such bean definition found.");
    }

    // Constructor with a custom message
    public CreateBeanInstanceErrorException(String message) {
        super(message);
    }

    // Constructor with a custom message and a cause
    public CreateBeanInstanceErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with only a cause
    public CreateBeanInstanceErrorException(Throwable cause) {
        super(cause);
    }
}
