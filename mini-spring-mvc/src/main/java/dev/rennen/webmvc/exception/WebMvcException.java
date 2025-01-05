package dev.rennen.webmvc.exception;

/**
 * 2025/1/5 17:31
 *
 * @author rennen.dev
 */
public class WebMvcException extends RuntimeException {

    public WebMvcException() {
    }

    public WebMvcException(String message) {
        super(message);
    }

    public WebMvcException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebMvcException(Throwable cause) {
        super(cause);
    }

}
