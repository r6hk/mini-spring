package dev.rennen.event.core;

/**
 * @author rennen.dev
 * @since 2024/12/30 17:35
 */
public interface ApplicationEventPublisher {
    void publishEvent(Object event);

    default void publishEvent(ApplicationEvent event) {
        publishEvent((Object) event);
    }
}