package dev.rennen.event;

/**
 * @author rennen.dev
 * @since 2024/12/30 17:35
 */
public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);
}
