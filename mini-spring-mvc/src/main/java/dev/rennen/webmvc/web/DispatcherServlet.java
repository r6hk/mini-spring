package dev.rennen.webmvc.web;

import dev.rennen.webmvc.context.WebApplicationContext;
import dev.rennen.webmvc.exception.InternalServerException;
import dev.rennen.webmvc.util.annotation.RequestMapping;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * 2025/1/5 17:06
 *
 * @author rennen.dev
 */
@Slf4j
public class DispatcherServlet extends HttpServlet {

    /**
     * URL 对应的 Class TODO: 待删除
     */
    private final Map<String, Class<?>> mappingClz = new HashMap<>();

    /**
     * URL 对应的方法
     */
    private final Map<String, Object> mappingObjs = new HashMap<>();

    /**
     * 存储需要扫描的 package 列表
     */
    private final List<String> packageNames = new ArrayList<>();

    /**
     * 存储 controller 名称与对象的映射关系
     */
    private final Map<String,Object> controllerObjs = new HashMap<>();

    /**
     * 用于存储 controller 名称数组列表
     */
    private final List<String> controllerNames = new ArrayList<>();

    /**
     * 用于存储 controller 名称与类的映射关系
     */
    private final Map<String,Class<?>> controllerClasses = new HashMap<>();

    /**
     * 保存自定义的 @RequestMapping 名称（URL 的名称）的列表
     */
    private List<String> urlMappingNames = new ArrayList<>();

    /**
     * 保存 URL 名称与方法之间的映射关系
     */
    private final Map<String,Method> mappingMethods = new HashMap<>();

    private WebApplicationContext webApplicationContext;


    /**
     * 配置文件路径
     */
    private String sContextConfigLocation;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.webApplicationContext = (WebApplicationContext) this.getServletContext()
                        .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        this.sContextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(sContextConfigLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.packageNames.addAll(XmlScanComponentHelper.getNodeValue(xmlPath));
        refresh();
    }

    /**
     * 对所有的 mappingValues 中注册的类进行实例化，默认构造函数
     */
    private void refresh() {
        initController();
        initMapping();
    }

    protected void initController() {
        //扫描包，获取所有类名
        this.controllerNames.addAll(scanPackages(this.packageNames));
        for (String controllerName : this.controllerNames) {
            Object obj = null;
            Class<?> clz = null;
            try {
                clz = Class.forName(controllerName); //加载类
                this.controllerClasses.put(controllerName, clz);
            } catch (Exception e) {
                throw new InternalServerException("Class not found", e);
            }
            try {
                obj = clz.getDeclaredConstructor().newInstance(); //实例化bean
                this.controllerObjs.put(controllerName, obj);
            } catch (Exception e) {
                throw new InternalServerException("Instance failed", e);
            }
        }
    }

    private List<String> scanPackages(List<String> packages) {
        List<String> tempControllerNames = new ArrayList<>();
        for (String packageName : packages) {
            tempControllerNames.addAll(scanPackage(packageName));
        }
        return tempControllerNames;
    }

    private List<String> scanPackage(String packageName) {
        List<String> tempControllerNames = new ArrayList<>();
        URI uri = null;
        //将以.分隔的包名换成以/分隔的uri
        try {
            URL url = this.getClass().getResource("/" + packageName.replaceAll("\\.", "/"));
            uri = url.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File dir = new File(uri);
        //处理对应的文件目录
        for (File file : Objects.requireNonNull(dir.listFiles())) { //目录下的文件或者子目录
            if (file.isDirectory()) { //对子目录递归扫描
                scanPackage(packageName + "." + file.getName());
            } else { //类文件
                String controllerName = packageName + "."
                        + file.getName().replace(".class", "");
                tempControllerNames.add(controllerName);
            }
        }
        return tempControllerNames;
    }

    protected void initMapping() {
        for (String controllerName : this.controllerNames) {
            Class<?> clazz = this.controllerClasses.get(controllerName);
            Object obj = this.controllerObjs.get(controllerName);
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                //检查所有的方法
                boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                if (isRequestMapping) { //有RequestMapping注解
                    String methodName = method.getName();
                    //建立方法名和URL的映射
                    String urlMapping = method.getAnnotation(RequestMapping.class).value();
                    this.urlMappingNames.add(urlMapping);
                    this.mappingObjs.put(urlMapping, obj);
                    this.mappingMethods.put(urlMapping, method);
                }
            }
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String sPath = request.getServletPath(); //获取请求的path
        if (!this.urlMappingNames.contains(sPath)) {
            // 错误处理
            response.getWriter().append("404 Not Found");
            return;
        }

        Object obj = this.mappingObjs.get(sPath);  //获取bean实例
        Object objResult = null;
        try {
            Method method = this.mappingMethods.get(sPath); //获取方法
            objResult = method.invoke(obj); //方法调用
            response.getWriter().append(objResult.toString());
        } catch (Exception e) {
            response.getWriter().append("500 Internal Server Error");
        }
        //将方法返回值写入response
    }

}
