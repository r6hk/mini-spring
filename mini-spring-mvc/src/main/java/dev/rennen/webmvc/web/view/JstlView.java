package dev.rennen.webmvc.web.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

import java.util.Map;

/**
 * 2025/1/27 9:24
 *
 * @author rennen.dev
 */
@Data
public class JstlView implements View {
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=ISO-8859-1";
    private String contentType = DEFAULT_CONTENT_TYPE;
    private String requestContextAttribute;
    private String beanName;
    private String url;

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (Map.Entry<String, ?> e : model.entrySet()) {
            request.setAttribute(e.getKey(), e.getValue());
        }
        request.getRequestDispatcher(getUrl()).forward(request, response);
    }
}