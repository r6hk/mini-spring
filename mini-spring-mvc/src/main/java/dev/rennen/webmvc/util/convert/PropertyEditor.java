package dev.rennen.webmvc.util.convert;

/**
 * 2025/1/26 10:31
 *
 * @author rennen.dev
 */
public interface PropertyEditor {
    void setAsText(String text);

    void setValue(Object value);

    Object getValue();

    Object getAsText();
}