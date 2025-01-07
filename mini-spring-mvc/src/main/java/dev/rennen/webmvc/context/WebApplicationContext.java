package dev.rennen.webmvc.context;

import dev.rennen.beans.factory.support.ApplicationContext;
import jakarta.servlet.ServletContext;

/**
 * 2025/1/7 16:19
 *
 * @author rennen.dev
 */
public interface WebApplicationContext extends ApplicationContext {
    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";

    ServletContext getServletContext();

    void setServletContext(ServletContext servletContext);
}
