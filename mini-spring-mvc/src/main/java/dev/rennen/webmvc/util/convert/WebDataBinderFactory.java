package dev.rennen.webmvc.util.convert;

import dev.rennen.webmvc.web.WebDataBinder;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 2025/1/26 11:03
 *
 * @author rennen.dev
 */
public class WebDataBinderFactory {

    public WebDataBinder createBinder(HttpServletRequest request, Object target, String objectName) {
        WebDataBinder wbd = new WebDataBinder(target, objectName);
        initBinder(wbd, request);
        return wbd;
    }

    protected void initBinder(WebDataBinder dataBinder, HttpServletRequest request) {
        // do nothing, just for subclass to override
    }
}