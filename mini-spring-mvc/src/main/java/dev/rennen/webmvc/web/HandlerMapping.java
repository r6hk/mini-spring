package dev.rennen.webmvc.web;

import dev.rennen.beans.factory.support.ApplicationContext;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 2025/1/8 17:15
 *
 * @author rennen.dev
 */
public interface HandlerMapping {
    HandlerMethod getHandler(HttpServletRequest request) throws Exception;

    void initMappings(ApplicationContext applicationContext);
}
