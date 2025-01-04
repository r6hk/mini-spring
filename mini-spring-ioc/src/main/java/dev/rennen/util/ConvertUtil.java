package dev.rennen.util;

/**
 * @author rennen.dev
 * @since 2025/1/3 18:28
 */
public class ConvertUtil {
    public static Object convertStringToType(Class<?> targetType, String value) throws Exception {
        if (targetType == String.class) {
            return value;
        } else if (targetType == Integer.class || targetType == int.class) {
            return Integer.parseInt(value);
        } else if (targetType == Long.class || targetType == long.class) {
            return Long.parseLong(value);
        } else if (targetType == Double.class || targetType == double.class) {
            return Double.parseDouble(value);
        } else if (targetType == Float.class || targetType == float.class) {
            return Float.parseFloat(value);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (targetType == Character.class || targetType == char.class) {
            if (value.length() != 1) {
                throw new IllegalArgumentException("Cannot convert to Character: " + value);
            }
            return value.charAt(0);
        } else if (targetType == Short.class || targetType == short.class) {
            return Short.parseShort(value);
        } else if (targetType == Byte.class || targetType == byte.class) {
            return Byte.parseByte(value);
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + targetType.getName());
        }
    }
}
