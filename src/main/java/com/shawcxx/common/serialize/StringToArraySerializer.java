package com.shawcxx.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author cjl
 * @date 2022/2/28 10:16
 * @description 序列化方法, 将字符串根据 ',' 拆分为数组
 */
public class StringToArraySerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String splitter;
        if (value.contains("||")) {
            splitter = "\\|\\|";
        } else {
            splitter = ",";
        }
        String[] list = value.split(splitter);
        gen.writeArray(list, 0, list.length);
    }
}
