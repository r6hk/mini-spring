package dev.rennen.webmvc.web;

import dev.rennen.beans.inject.PropertyValues;
import dev.rennen.webmvc.util.convert.BeanWrapperImpl;
import dev.rennen.webmvc.util.convert.PropertyEditor;
import dev.rennen.webmvc.util.convert.WebUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * 用于将 request 请求内的字符串参数转换成不同类型的参数，来进行适配
 * 2025/1/18 11:26
 *
 * @author rennen.dev
 */
public class WebDataBinder {
    private Object target;
    private Class<?> clz;
    private String objectName;

    public WebDataBinder(Object target) {
        this(target, "");
    }

    public WebDataBinder(Object target, String targetName) {
        this.target = target;
        this.objectName = targetName;
        this.clz = this.target.getClass();
    }

    //核心绑定方法，将request里面的参数值绑定到目标对象的属性上
    public void bind(HttpServletRequest request) {
        PropertyValues mpvs = assignParameters(request);
        addBindValues(mpvs, request);
        doBind(mpvs);
    }

    private void doBind(PropertyValues mpvs) {
        applyPropertyValues(mpvs);
    }

    //实际将参数值与对象属性进行绑定的方法
    protected void applyPropertyValues(PropertyValues mpvs) {
        getPropertyAccessor().setPropertyValues(mpvs);
    }

    //设置属性值的工具
    protected BeanWrapperImpl getPropertyAccessor() {
        return new BeanWrapperImpl(this.target);
    }

    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        getPropertyAccessor().registerCustomEditor(requiredType, propertyEditor);
    }

    //将Request参数解析成PropertyValues
    private PropertyValues assignParameters(HttpServletRequest request) {
        Map<String, Object> map = WebUtils.getParametersStartingWith(request, "");
        return new PropertyValues(map);
    }

    protected void addBindValues(PropertyValues mpvs, HttpServletRequest request) {
        // do nothing, just for subclass to override
    }
}