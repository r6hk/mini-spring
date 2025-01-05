package dev.rennen.event.core;

import java.util.EventListener;

/**
 * @author rennen.dev
 * @since 2025/1/2 15:51
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
    void onApplicationEvent(E event);
}
