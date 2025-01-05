package dev.rennen.webmvc.web;

import dev.rennen.beans.define.ClassPathXmlResource;
import dev.rennen.beans.define.Resource;
import dev.rennen.webmvc.exception.CreateDispatcherServletException;
import dev.rennen.webmvc.exception.InternalServerException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 2025/1/5 17:06
 *
 * @author rennen.dev
 */
@Slf4j
public class DispatcherServlet extends HttpServlet {

    /**
     * URL 对应的 MappingValue
     */
    private Map<String, MappingValue> mappingValues;

    /**
     * URL 对应的 Class
     */
    private Map<String, Class<?>> mappingClz = new HashMap<>();

    /**
     * URL 对应的方法
     */
    private Map<String, Object> mappingObjs = new HashMap<>();

    /**
     * 配置文件路径
     */
    private String sContextConfigLocation;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        sContextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = this.getClass().getClassLoader().getResource(sContextConfigLocation);
        Resource resource = new ClassPathXmlResource(sContextConfigLocation);
        XmlConfigReader reader = new XmlConfigReader();
        mappingValues = reader.loadConfig(resource);
        refresh();
    }

    /**
     * 对所有的 mappingValues 中注册的类进行实例化，默认构造函数
     */
    private void refresh() {
        for (Map.Entry<String, MappingValue> entry : mappingValues.entrySet()) {
            String id = entry.getKey();
            String className = entry.getValue().getClz();
            Object obj = null;
            Class<?> clz = null;
            try {
                clz = Class.forName(className);
                obj = clz.getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new CreateDispatcherServletException("Create DispatcherServlet failed", e);
            }
            mappingClz.put(id, clz);
            mappingObjs.put(id, obj);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sPath = request.getServletPath(); //获取请求的path
        if (this.mappingValues.get(sPath) == null) {
            return;
        }

        Class<?> clz = this.mappingClz.get(sPath); //获取bean类定义
        Object obj = this.mappingObjs.get(sPath);  //获取bean实例
        String methodName = this.mappingValues.get(sPath).getMethod(); //获取调用方法名
        Object objResult = null;
        try {
            Method method = clz.getMethod(methodName);
            objResult = method.invoke(obj); //方法调用
        } catch (Exception e) {
            throw new InternalServerException("Method invoke failed", e);
        }
        //将方法返回值写入response
        response.getWriter().append(objResult.toString());
    }




}
