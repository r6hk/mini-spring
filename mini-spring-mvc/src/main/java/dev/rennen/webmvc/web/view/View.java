package dev.rennen.webmvc.web.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * 2025/1/27 9:01
 *
 * @author rennen.dev
 */
public interface View {
    void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception;

    default String getContentType() {
        return null;
    }

    void setContentType(String contentType);

    void setUrl(String url);

    String getUrl();

    void setRequestContextAttribute(String requestContextAttribute);

    String getRequestContextAttribute();

}
