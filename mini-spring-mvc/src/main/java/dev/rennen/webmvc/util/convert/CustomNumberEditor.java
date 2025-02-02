package dev.rennen.webmvc.util.convert;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;

/**
 * 2025/1/26 10:31
 *
 * @author rennen.dev
 */
public class CustomNumberEditor implements PropertyEditor {

    private Class<? extends Number> numberClass; //数据类型
    private NumberFormat numberFormat; //指定格式
    private boolean allowEmpty;
    @Getter
    private Object value;

    public CustomNumberEditor(Class<? extends Number> numberClass, boolean allowEmpty) throws IllegalArgumentException {
        this(numberClass, null, allowEmpty);
    }

    public CustomNumberEditor(Class<? extends Number> numberClass, NumberFormat numberFormat, boolean allowEmpty) throws IllegalArgumentException {
        this.numberClass = numberClass;
        this.numberFormat = numberFormat;
        this.allowEmpty = allowEmpty;
    }

    //将一个字符串转换成number赋值
    public void setAsText(String text) {
        if (this.allowEmpty && StringUtils.isNotBlank(text)) {
            setValue(null);
        } else if (this.numberFormat != null) {
            // 给定格式
            setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
        } else {
            setValue(NumberUtils.parseNumber(text, this.numberClass));
        }
    }

    //接收Object作为参数
    public void setValue(Object value) {
        if (value instanceof Number number) {
            this.value = (NumberUtils.convertNumberToTargetClass(number, this.numberClass));
        } else {
            this.value = value;
        }
    }

    //将number表示成格式化串
    public Object getAsText() {
        Object value = this.value;
        if (value == null) {
            return "";
        }
        if (this.numberFormat != null) {
            // 给定格式.
            return this.numberFormat.format(value);
        } else {
            return value.toString();
        }
    }
}
