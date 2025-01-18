package dev.rennen.webmvc.web.listener;

import dev.rennen.webmvc.context.WebApplicationContext;
import dev.rennen.webmvc.context.XmlWebApplicationContext;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

    /**
     * web.xml 中配置的 contextConfigLocation 参数。
     * <br/><br/>
     * example:
     *
     * <pre>
     * {@code
     *   <context-param>
     *     <param-name>contextConfigLocation</param-name>
     *     <param-value>applicationContext.xml</param-value>
     *   </context-param>
     * }
     * </pre>
     */
    public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";

    private WebApplicationContext context;

    public ContextLoaderListener() {
    }

    public ContextLoaderListener(WebApplicationContext context) {
        this.context = context;
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // do nothing
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        initWebApplicationContext(event.getServletContext());
    }

    private void initWebApplicationContext(ServletContext servletContext) {
        String sContextLocation = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
        WebApplicationContext wac = new XmlWebApplicationContext(sContextLocation);
        wac.setServletContext(servletContext);
        this.context = wac;
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
    }

}
