package dev.rennen.webmvc.web;

import dev.rennen.webmvc.context.WebApplicationContext;
import dev.rennen.webmvc.util.convert.WebDataBinderFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 2025/1/8 17:28
 *
 * @author rennen.dev
 */
@Slf4j
public class RequestMappingHandlerAdapter implements HandlerAdapter {

    WebApplicationContext wac;

    public RequestMappingHandlerAdapter(WebApplicationContext wac) {
        this.wac = wac;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        handleInternal(request, response, (HandlerMethod) handler);
    }

    private void handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler)
            throws IOException {
        Method method = handler.getMethod();
        Object obj = handler.getBean();
        Object objResult = null;
        try {
            objResult = method.invoke(obj);
            response.getWriter().append(objResult.toString());
        } catch (Exception e) {
            log.error("handleInternal error", e);
            response.getWriter().append("500 internal server error");
        }
    }

    protected void invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response,
                                       HandlerMethod handlerMethod) throws Exception {
        WebDataBinderFactory binderFactory = new WebDataBinderFactory();
        Parameter[] methodParameters =
                handlerMethod.getMethod().getParameters();
        Object[] methodParamObjs = new Object[methodParameters.length];
        int i = 0;
        //对调用方法里的每一个参数，处理绑定
        for (Parameter methodParameter : methodParameters) {
            Object methodParamObj = methodParameter.getType().newInstance();
            //给这个参数创建WebDataBinder
            WebDataBinder wdb = binderFactory.createBinder(request,
                    methodParamObj, methodParameter.getName());
            wdb.bind(request);
            methodParamObjs[i] = methodParamObj;
            i++;
        }
        Method invocableMethod = handlerMethod.getMethod();
        Object returnObj = invocableMethod.invoke(handlerMethod.getBean(), methodParamObjs);
        response.getWriter().append(returnObj.toString());
    }
}

