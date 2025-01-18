package dev.rennen.webmvc.web;

import dev.rennen.webmvc.context.AnnotationConfigWebApplicationContext;
import dev.rennen.webmvc.context.WebApplicationContext;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

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

        /**
         * 配置文件路径
         */
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
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webApplicationContext);
        try {
            doDispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequest processedRequest = request;
        HandlerMethod handlerMethod = null;
        handlerMethod = this.handlerMapping.getHandler(processedRequest);
        if (handlerMethod == null) {
            response.getWriter().append("404 not found");
        }
        HandlerAdapter ha = this.handlerAdapter;
        ha.handle(processedRequest, response, handlerMethod);
    }

    public List<String> getUrlMappingNames() {
        return urlMappingNames;
    }

    public void setUrlMappingNames(List<String> urlMappingNames) {
        this.urlMappingNames = urlMappingNames;
    }
}
