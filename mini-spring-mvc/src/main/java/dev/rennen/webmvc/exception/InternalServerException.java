package dev.rennen.webmvc.exception;

/**
 * 2025/1/5 19:10
 *
 * @author rennen.dev
 */
public class InternalServerException extends WebMvcException {

        public InternalServerException(String message) {
            super(message);
        }

        public InternalServerException(String message, Throwable cause) {
            super(message, cause);
        }
}
