package dev.rennen.webmvc.web;

import dev.rennen.webmvc.web.view.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 2025/1/8 17:15
 *
 * @author rennen.dev
 */
public interface HandlerAdapter {
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
