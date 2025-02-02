package dev.rennen.webmvc.util.convert;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 让 Controller 返回的对象转换成字符串
 * 2025/1/26 12:11
 *
 * @author rennen.dev
 */
public interface HttpMessageConverter {
    void write(Object obj, HttpServletResponse response) throws IOException;
}