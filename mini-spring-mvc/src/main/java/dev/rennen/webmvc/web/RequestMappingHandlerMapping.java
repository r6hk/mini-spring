package dev.rennen.webmvc.web;

import dev.rennen.beans.factory.support.ApplicationContext;
import dev.rennen.webmvc.util.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;


/**
 * 2025/1/8 17:20
 *
 * @author rennen.dev
 */
@Slf4j
public class RequestMappingHandlerMapping implements HandlerMapping {

    ApplicationContext applicationContext;

    private final MappingRegistry mappingRegistry = new MappingRegistry();

    //建立URL与调用方法和实例的映射关系，存储在mappingRegistry中
    public void initMappings(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;

        Class<?> clz = null;
        Object obj = null;
        String[] controllerNames = this.applicationContext.getBeanDefinitionNames();
        Method[] methods = null;
        //扫描 WAC 中存放的所有 bean
        for (String controllerName : controllerNames) {
            try {
                clz = Class.forName(controllerName);
                obj = this.applicationContext.getBean(controllerName);
                methods = clz.getDeclaredMethods();
                //检查每一个方法声明
                for (Method method : methods) {
                    boolean isRequestMapping =
                            method.isAnnotationPresent(RequestMapping.class);
                    //如果该方法带有@RequestMapping注解,则建立映射关系
                    if (isRequestMapping) {
                        String urlMapping =
                                method.getAnnotation(RequestMapping.class).value();

                        this.mappingRegistry.getUrlMappingNames().add(urlMapping);
                        this.mappingRegistry.getMappingObjs().put(urlMapping,
                                obj);
                        this.mappingRegistry.getMappingMethods().put(urlMapping,
                                method);
                    }
                }
            } catch (Exception e) {
                log.error("initMapping error", e);
            }
        }
    }

    //根据访问URL查找对应的调用方法
    public HandlerMethod getHandler(HttpServletRequest request) throws Exception {
        String sPath = request.getServletPath();
        if (!this.mappingRegistry.getUrlMappingNames().contains(sPath)) {
            return null;
        }
        Method method = this.mappingRegistry.getMappingMethods().get(sPath);
        Object obj = this.mappingRegistry.getMappingObjs().get(sPath);
        return new HandlerMethod(method, obj);
    }

}
