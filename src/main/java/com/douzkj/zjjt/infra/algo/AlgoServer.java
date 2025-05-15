package com.douzkj.zjjt.infra.algo;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONNull;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.douzkj.zjjt.infra.algo.entity.CleanupCount;
import com.douzkj.zjjt.web.param.CleanupRequest;
import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@Data
@Slf4j
public class AlgoServer {

    private final AlgoServerProps algoServerProps;

    private static final DateTimeFormatter CLEANUP_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    /**
     * 读取帧
     * @param rtspUrl
     * @return
     */
    public Response<String> readFrame(String rtspUrl) {
        return get("/rtsp/frame", ImmutableMap.of("url", rtspUrl), String.class);
    }

    public Response<CleanupCount> cleanupSimilarImages(CleanupRequest cleanupRequest) {
        Map<String, Object> params = ImmutableMap.of(
                "folder", cleanupRequest.getFolder(),
                "start_time", cleanupRequest.getStart().format(CLEANUP_DATE_FORMAT),
                "end_time", cleanupRequest.getEnd().format(CLEANUP_DATE_FORMAT)
        );
        return get("/task/cleanup/similar",params , CleanupCount.class);
    }

    protected <T> Response<T> unSerializer(String res, Class<T> clazz) {
        JSONObject entries = JSONUtil.parseObj(res);
        Response<T> response = new Response<>();
        response.setCode(entries.getInt("code"));
        response.setMsg(entries.getStr("msg"));
        Object dataObj = entries.get("data");
        if (dataObj != null && !JSONNull.NULL.equals(dataObj)) {
            if (clazz.isPrimitive() || isWrapperClass(clazz)) {
                // 处理基础类型及其包装类
                dataObj = convertPrimitive(dataObj, clazz);
                response.setData(clazz.cast(dataObj));
            } else if (dataObj instanceof JSONObject) {
                response.setData(((JSONObject) dataObj).toBean(clazz));
            } else {
                // 处理 data 不是 JSON 对象的情况
                response.setData(JSONUtil.toBean(JSONUtil.toJsonStr(dataObj), clazz));
            }
        }
        return response;
    }

    protected  <T> Response<T> get(String path, Map<String, Object> params, Class<T> clazz) {
        String url = algoServerProps.getEndpoint() + path;
        String s = HttpUtil.get(url, params);
        log.info("get url:{} \t ----> res: {}", url, s);
        return unSerializer(s, clazz);
    }

    protected  <T> Response<T> post(String path, Object data, Class<T> clazz) {
        String url = algoServerProps.getEndpoint() + path;
        String s = HttpUtil.post(url, JSONUtil.toJsonStr(data));
        return unSerializer(s, clazz);
    }

    // 判断是否为包装类
    private boolean isWrapperClass(Class<?> clazz) {
        return clazz == Integer.class || clazz == Long.class || clazz == Short.class ||
                clazz == Byte.class || clazz == Double.class || clazz == Float.class ||
                clazz == Boolean.class || clazz == Character.class || clazz == String.class;
    }

    // 转换基础类型
    @SuppressWarnings("unchecked")
    private <T> T convertPrimitive(Object dataObj, Class<T> clazz) {
        if (clazz == Integer.class || clazz == int.class) {
            return (T) Integer.valueOf(dataObj.toString());
        } else if (clazz == Long.class || clazz == long.class) {
            return (T) Long.valueOf(dataObj.toString());
        } else if (clazz == Short.class || clazz == short.class) {
            return (T) Short.valueOf(dataObj.toString());
        } else if (clazz == Byte.class || clazz == byte.class) {
            return (T) Byte.valueOf(dataObj.toString());
        } else if (clazz == Double.class || clazz == double.class) {
            return (T) Double.valueOf(dataObj.toString());
        } else if (clazz == Float.class || clazz == float.class) {
            return (T) Float.valueOf(dataObj.toString());
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            return (T) Boolean.valueOf(dataObj.toString());
        } else if (clazz == Character.class || clazz == char.class) {
            if (dataObj.toString().length() > 0) {
                return (T) Character.valueOf(dataObj.toString().charAt(0));
            }
        }
        return (T) dataObj;
    }



}
