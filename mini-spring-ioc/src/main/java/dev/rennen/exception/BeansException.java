package dev.rennen.exception;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author rennen.dev
 * @since 2024/12/27 17:30
 */
public class BeansException extends Exception {

    public BeansException(String message) {
        super(message);
    }

    public BeansException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeansException(Throwable cause) {
        super(cause);
    }
}
