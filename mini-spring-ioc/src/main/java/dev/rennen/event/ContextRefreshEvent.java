package dev.rennen.event;

import dev.rennen.event.core.ApplicationEvent;

import java.io.Serial;

/**
 * @author rennen.dev
 * @since 2025/1/2 15:56
 */
public class ContextRefreshEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = 1L;

    public ContextRefreshEvent(Object source) {
        super(source);
    }

    public String toString() {
        return this.msg;
    }
}
