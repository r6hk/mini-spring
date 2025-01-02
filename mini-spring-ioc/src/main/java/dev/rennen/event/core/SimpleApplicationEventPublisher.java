package dev.rennen.event.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rennen.dev
 * @since 2025/1/2 16:03
 */
public class SimpleApplicationEventPublisher implements ApplicationEventPublisher{
    List<ApplicationListener> listeners = new ArrayList<>();

    @Override
    public void publishEvent(ApplicationEvent event) {
        for (ApplicationListener listener : listeners) {
            listener.onApplicationEvent(event);
        }
    }

    @Override
    public void addListener(ApplicationListener listener) {
        listeners.add(listener);
    }
}
