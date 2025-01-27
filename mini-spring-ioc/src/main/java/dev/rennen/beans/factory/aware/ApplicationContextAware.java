package dev.rennen.beans.factory.aware;

import dev.rennen.beans.factory.support.ApplicationContext;

/**
 * 2025/1/27 10:52
 *
 * @author rennen.dev
 */
public interface ApplicationContextAware {
    void setApplicationContext(ApplicationContext applicationContext);
}
