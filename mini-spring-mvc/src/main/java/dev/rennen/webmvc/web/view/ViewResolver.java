package dev.rennen.webmvc.web.view;

/**
 * 2025/1/27 9:22
 *
 * @author rennen.dev
 */
public interface ViewResolver {
    View resolveViewName(String viewName) throws Exception;
}
