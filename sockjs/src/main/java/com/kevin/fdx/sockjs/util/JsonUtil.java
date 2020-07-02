package com.kevin.fdx.sockjs.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final SimpleDateFormat DEFAULT_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(DEFAULT_DATEFORMAT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtil() {
        // no constructor function
    }

    public static String bean2Json(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("jackson序列化异常", e);
            return null;
        }
    }

    public static String bean2Json(Object obj, String dateFormat) {
        String jsonStr = null;
        objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
        try {
            jsonStr = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("jackson序列化异常", e);
            return null;
        }
        objectMapper.setDateFormat(DEFAULT_DATEFORMAT);
        return jsonStr;
    }

    public static <T> T json2Bean(String jsonStr, Class<T> objClass) {
        try {
            return objectMapper.readValue(jsonStr, objClass);
        } catch (IOException e) {
            log.error("jackson反序列化异常", e);
            return null;
        }
    }

    public static <T> List<T> json2List(String jsonArr, Class<T> clazz) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
            return objectMapper.readValue(jsonArr, javaType);
        } catch (IOException e) {
            log.error("jackson反序列化异常", e);
            return null;
        }
    }

    public static <T> T map2Bean(Map<String, Object> map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }
}
