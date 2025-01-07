package dev.rennen.webmvc.context;

import dev.rennen.beans.factory.ClassPathXmlApplicationContext;
import jakarta.servlet.ServletContext;

/**
 * 2025/1/7 16:27
 *
 * @author rennen.dev
 */
public class AnnotationConfigWebApplicationContext extends ClassPathXmlApplicationContext implements WebApplicationContext {

    private ServletContext servletContext;

    public AnnotationConfigWebApplicationContext(String fileName) {
        // 配置文件放在 resource 目录下
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
