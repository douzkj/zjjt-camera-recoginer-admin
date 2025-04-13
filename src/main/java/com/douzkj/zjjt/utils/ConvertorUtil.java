package com.douzkj.zjjt.utils;

import cn.hutool.json.JSONUtil;
import org.mapstruct.Named;
import org.springframework.boot.context.properties.bind.Name;

public class ConvertorUtil {

    @Named("objToJson")
    public String objToJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return JSONUtil.toJsonStr(obj);
    }


    @Named("jsonToObj")
    public <T> T jsonToObj(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        return JSONUtil.toBean(json, clazz);
    }
}
