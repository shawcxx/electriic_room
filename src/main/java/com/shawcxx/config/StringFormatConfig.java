package com.shawcxx.config;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author cjl
 * @description jackson反序列化对象时的去空格操作和空字符串处理
 * @date 2020/5/18 19:38
 */
@Configuration
public class StringFormatConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {

        return builder -> {
            builder.deserializers(new StdScalarDeserializer<String>(String.class) {
                @Override
                public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
//                    if (StrUtil.isBlank(jsonParser.getValueAsString())) {
//                        return null;
//                    }
                    return StrUtil.trim(jsonParser.getValueAsString());
                }
            });
        };
    }
}
