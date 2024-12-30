package dev.rennen.event;

import java.util.EventObject;

/**
 * @author rennen.dev
 * @since 2024/12/30 17:40
 */
public class ApplicationEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    public ApplicationEvent(Object source) {
        super(source);
    }

}
