package dev.rennen.webmvc.context;

import dev.rennen.beans.factory.ClassPathXmlApplicationContext;
import jakarta.servlet.ServletContext;
import lombok.NonNull;

/**
 * 2025/1/8 17:08
 *
 * @author rennen.dev
 */
public class XmlWebApplicationContext extends ClassPathXmlApplicationContext implements WebApplicationContext {

    private ServletContext servletContext;

    public XmlWebApplicationContext(@NonNull String fileName) {
        super(fileName);
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
