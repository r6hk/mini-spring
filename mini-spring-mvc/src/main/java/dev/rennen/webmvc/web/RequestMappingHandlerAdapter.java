package dev.rennen.webmvc.web;

import dev.rennen.beans.factory.aware.ApplicationContextAware;
import dev.rennen.beans.factory.support.ApplicationContext;
import dev.rennen.exception.BeansException;
import dev.rennen.webmvc.context.WebApplicationContext;
import dev.rennen.webmvc.util.convert.HttpMessageConverter;
import dev.rennen.webmvc.util.convert.WebBindingInitializer;
import dev.rennen.webmvc.util.convert.WebDataBinderFactory;
import dev.rennen.webmvc.web.annotations.PathVariable;
import dev.rennen.webmvc.web.annotations.ResponseBody;
import dev.rennen.webmvc.web.view.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 2025/1/8 17:28
 *
 * @author rennen.dev
 */
@Slf4j
public class RequestMappingHandlerAdapter implements HandlerAdapter, ApplicationContextAware {
    private ApplicationContext applicationContext = null;
    @Setter @Getter
    private WebBindingInitializer webBindingInitializer = null;
    private HttpMessageConverter messageConverter = null;

    public RequestMappingHandlerAdapter(WebApplicationContext wac) {
        this.applicationContext = wac;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return handleInternal(request, response, (HandlerMethod) handler);
    }

    private ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        ModelAndView mv = null;

        try {
            mv = invokeHandlerMethod(request, response, handler);
        } catch (Exception e) {
            log.error("invokeHandlerMethod error", e);
            throw new RuntimeException("invokeHandlerMethod error", e);
        }

        return mv;
    }

    protected ModelAndView invokeHandlerMethod(HttpServletRequest request,
                                               HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {

        Object[] methodParamObjs = dataBind(request, response, handlerMethod);

        Method invocableMethod = handlerMethod.getMethod();
        Object returnObj = invocableMethod.invoke(handlerMethod.getBean(), methodParamObjs);
        Class<?> returnType = invocableMethod.getReturnType();

        ModelAndView mav = null;
        if (invocableMethod.isAnnotationPresent(ResponseBody.class)){
            // 以 ResponseBody 注解的方法，返回值直接写入 response
            this.messageConverter.write(returnObj, response);
        }
        else if (returnType == void.class) {
            // do nothing
        } else {
            if (returnObj instanceof ModelAndView mavReturn) {
                mav = mavReturn;
            }
            else if(returnObj instanceof String sTarget) {
                mav = new ModelAndView();
                mav.setViewName(sTarget);
            }
        }

        return mav;
    }

    protected Object[] dataBind(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws InstantiationException, IllegalAccessException {
        WebDataBinderFactory binderFactory = new WebDataBinderFactory();

        Parameter[] methodParameters = handlerMethod.getMethod().getParameters();
        Object[] methodParamObjs = new Object[methodParameters.length];

        int i = 0;
        for (Parameter methodParameter : methodParameters) {
            if (methodParameter.getType()==HttpServletRequest.class) {
                methodParamObjs[i] = request;
            }
            else if (methodParameter.getType()==HttpServletResponse.class) {
                methodParamObjs[i] = response;
            }
            else if (methodParameter.isAnnotationPresent(PathVariable.class)) {
                // RESTful 形式的路径参数，简化处理
                String sServletPath = request.getServletPath();
                int index = sServletPath.lastIndexOf("/");
                String sParam = sServletPath.substring(index+1);
                if (int.class.isAssignableFrom(methodParameter.getType())) {
                    methodParamObjs[i] = Integer.parseInt(sParam);
                } else if (String.class.isAssignableFrom(methodParameter.getType())) {
                    methodParamObjs[i] = sParam;
                }
            }
            else if (methodParameter.getType()!=HttpServletRequest.class && methodParameter.getType()!=HttpServletResponse.class) {
                Object methodParamObj = methodParameter.getType().newInstance();
                WebDataBinder wdb = binderFactory.createBinder(request, methodParamObj, methodParameter.getName());
                webBindingInitializer.initBinder(wdb);
                wdb.bind(request);
                methodParamObjs[i] = methodParamObj;
            }

            i++;
        }
        return methodParamObjs;
    }

    /**
     * 容器注入接口
     * @param applicationContext 容器
     * @throws BeansException 异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}