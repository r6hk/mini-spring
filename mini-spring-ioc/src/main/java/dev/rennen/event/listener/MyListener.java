package dev.rennen.event.listener;

import dev.rennen.event.core.ApplicationListener;
import dev.rennen.event.eventobject.ContextRefreshedEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rennen.dev
 * @since 2025/1/4 14:46
 */
@Slf4j
@Getter
public class MyListener implements ApplicationListener<ContextRefreshedEvent> {
    private boolean isCalled = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        isCalled = true;
        log.info("MyListener is called!");
    }
}
