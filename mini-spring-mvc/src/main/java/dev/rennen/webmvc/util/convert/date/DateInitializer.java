package dev.rennen.webmvc.util.convert.date;

import dev.rennen.webmvc.util.convert.WebBindingInitializer;
import dev.rennen.webmvc.web.WebDataBinder;

import java.util.Date;

/**
 * 2025/1/26 11:31
 *
 * @author rennen.dev
 */
public class DateInitializer implements WebBindingInitializer {
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(Date.class,"yyyy-MM-dd", false));
    }
}