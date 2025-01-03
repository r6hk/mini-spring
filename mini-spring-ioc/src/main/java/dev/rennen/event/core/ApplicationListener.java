package dev.rennen.event.core;

import java.util.EventListener;

/**
 * @author rennen.dev
 * @since 2025/1/2 15:51
 */
public class ApplicationListener<T> implements EventListener {
    void onApplicationEvent(ApplicationEvent<T> event) {
        System.out.println("ApplicationListener: " + event.toString());
    }
}
