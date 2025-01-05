package dev.rennen.webmvc.exception;

/**
 * 2025/1/5 17:32
 *
 * @author rennen.dev
 */
public class CreateDispatcherServletException extends WebMvcException {

    public CreateDispatcherServletException(String message) {
        super(message);
    }

    public CreateDispatcherServletException(String message, Throwable cause) {
        super(message, cause);
    }
}
