package dev.rennen.event.eventobject;

import dev.rennen.event.core.ApplicationEvent;

import java.io.Serial;

/**
 * @author rennen.dev
 * @since 2025/1/2 15:56
 */
public class ContextRefreshedEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = 1L;

    public ContextRefreshedEvent(Object source) {
        super(source);
    }

    public String toString() {
        return this.msg;
    }
}
