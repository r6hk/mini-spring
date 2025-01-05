package dev.rennen.event.core;

import java.io.Serial;
import java.util.EventObject;

/**
 * @author rennen.dev
 * @since 2024/12/30 17:40
 */
public abstract class ApplicationEvent extends EventObject {
    @Serial
    private static final long serialVersionUID = 1L;

    protected String msg = null;

    public ApplicationEvent(Object source) {
        super(source);
        this.msg = source.toString();
    }

}
