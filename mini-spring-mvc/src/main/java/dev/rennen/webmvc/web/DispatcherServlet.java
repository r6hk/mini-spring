package dev.rennen.webmvc.web;

import dev.rennen.webmvc.context.AnnotationConfigWebApplicationContext;
import dev.rennen.webmvc.context.WebApplicationContext;
import dev.rennen.webmvc.web.view.ModelAndView;
import dev.rennen.webmvc.web.view.View;
import dev.rennen.webmvc.web.view.ViewResolver;
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
import java.util.Map;

/**
 * 2025/1/5 17:06
 *
 * @author rennen.dev
 */
@Slf4j
public class DispatcherServlet extends HttpServlet {

    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

    public static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";

    public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";

    public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";

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

    private HandlerMapping handlerMapping;

    private HandlerAdapter handlerAdapter;

    private ViewResolver viewResolver;

    public static final String PACKAGE_SCAN_PARAMETER = "packageScanLocation";

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
        initViewResolvers(this.webApplicationContext);
    }

    private void initHandlerMappings(WebApplicationContext wac) {
        this.handlerMapping = (HandlerMapping) wac.getBean(HANDLER_MAPPING_BEAN_NAME);
        this.handlerMapping.initMappings(wac);
    }

    private void initHandlerAdapters(WebApplicationContext wac) {
        this.handlerAdapter = (HandlerAdapter) wac.getBean(HANDLER_ADAPTER_BEAN_NAME);
    }

    private void initViewResolvers(WebApplicationContext wac) {
        this.viewResolver = (ViewResolver) wac.getBean(VIEW_RESOLVER_BEAN_NAME);
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
            // do nothing
        }
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethod handlerMethod = null;
        ModelAndView mv = null;

        handlerMethod = this.handlerMapping.getHandler(request);
        if (handlerMethod == null) {
            response.getWriter().append("404 Not Found");
        } else {
            HandlerAdapter ha = this.handlerAdapter;
            mv = ha.handle(request, response, handlerMethod);
            render(request, response, mv);
        }
    }

    private void render(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
        if (mv == null) {
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }

        String sTarget = mv.getViewName();
        Map<String, Object> modelMap = mv.getModel();
        View view = resolveViewName(sTarget);
        if (view == null) {
            response.getWriter().append("404 Not Found");
            return;
        }
        view.render(modelMap, request, response);
    }

    private View resolveViewName(String sTarget)
            throws Exception {
        if (this.viewResolver != null) {
            return this.viewResolver.resolveViewName(sTarget);
        }
        return null;
    }

}
