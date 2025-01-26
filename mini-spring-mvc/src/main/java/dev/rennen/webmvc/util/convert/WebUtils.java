package dev.rennen.webmvc.util.convert;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * 2025/1/26 10:21
 *
 * @author rennen.dev
 */
public class WebUtils {
    public static Map<String, Object> getParametersStartingWith(HttpServletRequest request, String prefix) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<>();
        if (prefix == null) {
            prefix = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (prefix.isEmpty() || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String value = request.getParameter(paramName);

                params.put(unprefixed, value);
            }
        }
        return params;
    }
}