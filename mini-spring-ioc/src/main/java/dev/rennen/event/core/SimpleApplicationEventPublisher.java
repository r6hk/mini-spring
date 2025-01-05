package dev.rennen.event.core;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SimpleApplicationEventPublisher implements ApplicationEventPublisher {
    private final List<ApplicationListener<? extends ApplicationEvent>> listeners = new ArrayList<>();

    // 动态添加新的 ApplicationListener
    public <E extends ApplicationEvent> void addApplicationListener(ApplicationListener<E> listener) {
        listeners.add(listener);
    }

    @Override
    public void publishEvent(Object event) {
        if (event instanceof ApplicationEvent applicationEvent) {
            // 遍历监听器并通知匹配的监听器
            for (ApplicationListener<? extends ApplicationEvent> listener : listeners) {
                if (supportsEvent(listener, applicationEvent)) {
                    notifyListener(listener, applicationEvent);
                }
            }
        }
    }

    // 判断监听器是否支持当前事件类型
    private boolean supportsEvent(ApplicationListener<?> listener, ApplicationEvent event) {
        Class<?> listenerType = getListenerType(listener);
        return listenerType != null && listenerType.isAssignableFrom(event.getClass());
    }

    // 使用泛型处理监听器通知
    @SuppressWarnings("unchecked")
    private <E extends ApplicationEvent> void notifyListener(ApplicationListener<E> listener, ApplicationEvent event) {
        listener.onApplicationEvent((E) event);
    }

    private Class<?> getListenerType(ApplicationListener<?> listener) {
        // 获取监听器实现的接口类型
        Type[] genericInterfaces = listener.getClass().getGenericInterfaces();

        for (Type type : genericInterfaces) {
            // 检查是否是 ApplicationListener 类型
            if (type instanceof ParameterizedType parameterizedType
                    && parameterizedType.getRawType() == ApplicationListener.class) {
                // 获取泛型参数
                Type eventType = parameterizedType.getActualTypeArguments()[0];
                if (eventType instanceof Class<?> clazz) {
                    return clazz; // 返回具体的事件类型
                }
            }
        }
        return null; // 如果未找到匹配的类型，返回 null
    }

}
