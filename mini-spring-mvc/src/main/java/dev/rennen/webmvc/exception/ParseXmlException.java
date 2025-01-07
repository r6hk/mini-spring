package dev.rennen.webmvc.exception;

/**
 * 2025/1/5 19:17
 *
 * @author rennen.dev
 */
public class ParseXmlException extends WebMvcException {
    public ParseXmlException(String message) {
        super(message);
    }

    public ParseXmlException(String message, Throwable cause) {
        super(message, cause);
    }
}
