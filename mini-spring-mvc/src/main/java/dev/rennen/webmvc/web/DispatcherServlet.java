package dev.rennen.webmvc.web;

import dev.rennen.webmvc.context.AnnotationConfigWebApplicationContext;
import dev.rennen.webmvc.context.WebApplicationContext;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 2025/1/5 17:06
 *
 * @author rennen.dev
 */
@Slf4j
public class DispatcherServlet extends HttpServlet {

    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

    /**
     * 保存自定义的 @RequestMapping 名称（URL 的名称）的列表
     */
    @Getter @Setter
    private List<String> urlMappingNames = new ArrayList<>();

    /**
     * 两级 ApplicationContext
     */
    private WebApplicationContext webApplicationContext;

    private WebApplicationContext parentApplicationContext;

    private RequestMappingHandlerMapping handlerMapping;

    private RequestMappingHandlerAdapter handlerAdapter;

    public final String PACKAGE_SCAN_PARAMETER = "packageScanLocation";

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        this.parentApplicationContext = (WebApplicationContext) this.getServletContext()
                .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        // 包扫描配置文件路径
        String sContextConfigLocation = config.getInitParameter(PACKAGE_SCAN_PARAMETER);

        this.webApplicationContext = new AnnotationConfigWebApplicationContext(sContextConfigLocation,
                this.parentApplicationContext);
        refresh();
    }

    /**
     * 对所有的 mappingValues 中注册的类进行实例化，默认构造函数
     */
    private void refresh() {
        initHandlerMappings(this.webApplicationContext);
        initHandlerAdapters(this.webApplicationContext);
    }

    private void initHandlerMappings(WebApplicationContext wac) {
        this.handlerMapping = new RequestMappingHandlerMapping(wac);
    }

    private void initHandlerAdapters(WebApplicationContext wac) {
        this.handlerAdapter = new RequestMappingHandlerAdapter(wac);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // note: 可以看一下父类的 service 是怎么实现的，以及为什么要重写
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webApplicationContext);
        try {
            doDispatch(request, response);
        } catch (Exception e) {
            log.error("doDispatch error", e);
            response.getWriter().append("500 Server Error");
        } finally {

        }
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethod handlerMethod = null;
        handlerMethod = this.handlerMapping.getHandler(request);
        if (handlerMethod == null) {
            response.getWriter().append("404 not found");
        }
        HandlerAdapter ha = this.handlerAdapter;
        ha.handle(request, response, handlerMethod);
    }

}
