package dev.rennen.webmvc.util.convert;

/**
 * 2025/1/27 8:43
 *
 * @author rennen.dev
 */
public interface ObjectMapper {
    void setDateFormat(String dateFormat);
    void setDecimalFormat(String decimalFormat);
    String writeValuesAsString(Object obj);
}
