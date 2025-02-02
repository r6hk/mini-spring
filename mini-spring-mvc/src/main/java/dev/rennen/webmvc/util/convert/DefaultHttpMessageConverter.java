package dev.rennen.webmvc.util.convert;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 2025/1/27 8:41
 *
 * @author rennen.dev
 */
public class DefaultHttpMessageConverter implements HttpMessageConverter {
    private static final String DEFAULT_CONTENT_TYPE = "text/json;charset=UTF-8";
    private static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
    ObjectMapper objectMapper;

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    public void write(Object obj, HttpServletResponse response) throws IOException {
        response.setContentType(DEFAULT_CONTENT_TYPE);
        response.setCharacterEncoding(DEFAULT_CHARACTER_ENCODING);
        writeInternal(obj, response);
        response.flushBuffer();
    }
    private void writeInternal(Object obj, HttpServletResponse response) throws IOException{
        String sJsonStr = this.objectMapper.writeValuesAsString(obj);
        PrintWriter pw = response.getWriter();
        pw.write(sJsonStr);
    }
}

