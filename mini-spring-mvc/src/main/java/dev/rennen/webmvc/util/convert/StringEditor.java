package dev.rennen.webmvc.util.convert;

import lombok.Getter;
import lombok.Setter;

/**
 * 2025/1/26 10:54
 *
 * @author rennen.dev
 */
public class StringEditor implements PropertyEditor {
    private Class<String> strClass;
    private String strFormat;
    private boolean allowEmpty;
    @Getter @Setter
    private Object value;

    public StringEditor(Class<String> strClass,
                        boolean allowEmpty) throws IllegalArgumentException {
        this(strClass, "", allowEmpty);
    }

    public StringEditor(Class<String> strClass,
                        String strFormat, boolean allowEmpty) throws IllegalArgumentException {
        this.strClass = strClass;
        this.strFormat = strFormat;
        this.allowEmpty = allowEmpty;
    }

    public void setAsText(String text) {
        setValue(text);
    }

    public String getAsText() {
        return value.toString();
    }
}