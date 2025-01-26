package dev.rennen.webmvc.util.convert;

import dev.rennen.webmvc.web.WebDataBinder;

/**
 * 2025/1/26 11:24
 *
 * @author rennen.dev
 */
public interface WebBindingInitializer {
    void initBinder(WebDataBinder binder);
}
