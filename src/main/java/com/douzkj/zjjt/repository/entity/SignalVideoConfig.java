package com.douzkj.zjjt.repository.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Serializable;

/** 识别命中后的录像配置；旧通路默认不录像。 */
@Data
public class SignalVideoConfig implements Serializable {

    @NotNull(message = "命中录像开关不能为空")
    private Boolean enabled = false;

    @NotNull(message = "录像时长不能为空")
    @Min(value = 1, message = "录像时长必须是大于等于 1 的整数秒")
    @JsonDeserialize(using = DurationSecondsDeserializer.class)
    private Integer durationSeconds = 60;

    /** 不提供时由算法端使用 frame.storage.frameStoragePath 下的 videos 目录。 */
    private String path;

    /** 只约束本字段，防止 Jackson 把小数静默截断为整数，不改变其他接口的解析规则。 */
    public static class DurationSecondsDeserializer extends StdDeserializer<Integer> {
        public DurationSecondsDeserializer() {
            super(Integer.class);
        }

        @Override
        public Integer deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            if (!parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                return (Integer) context.handleUnexpectedToken(Integer.class, parser);
            }
            return parser.getIntValue();
        }
    }
}
