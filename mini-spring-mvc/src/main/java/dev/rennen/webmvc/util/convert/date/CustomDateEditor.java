package dev.rennen.webmvc.util.convert.date;

import dev.rennen.webmvc.util.convert.PropertyEditor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 2025/1/26 11:21
 *
 * @author rennen.dev
 */
public class CustomDateEditor implements PropertyEditor {
    private Class<Date> dateClass;
    private DateTimeFormatter datetimeFormatter;
    private boolean allowEmpty;
    private Date value;

    public CustomDateEditor() throws IllegalArgumentException {
        this(Date.class, "yyyy-MM-dd", true);
    }

    public CustomDateEditor(Class<Date> dateClass) throws IllegalArgumentException {
        this(dateClass, "yyyy-MM-dd", true);
    }

    public CustomDateEditor(Class<Date> dateClass,
                            boolean allowEmpty) throws IllegalArgumentException {
        this(dateClass, "yyyy-MM-dd", allowEmpty);
    }

    public CustomDateEditor(Class<Date> dateClass,
                            String pattern, boolean allowEmpty) throws IllegalArgumentException {
        this.dateClass = dateClass;
        this.datetimeFormatter = DateTimeFormatter.ofPattern(pattern);
        this.allowEmpty = allowEmpty;
    }

    public void setAsText(String text) {
        if (this.allowEmpty && StringUtils.isNotBlank(text)) {
            setValue(null);
        } else {
            LocalDate localdate = LocalDate.parse(text, datetimeFormatter);
            setValue(Date.from(localdate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
    }

    public void setValue(Object value) {
        this.value = (Date) value;
    }

    public String getAsText() {
        Date value = this.value;
        if (value == null) {
            return "";
        } else {
            LocalDate localDate = value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return localDate.format(datetimeFormatter);
        }
    }

    public Object getValue() {
        return this.value;
    }
}