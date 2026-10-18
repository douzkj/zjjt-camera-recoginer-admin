package com.douzkj.zjjt;

import cn.hutool.json.JSONUtil;
import com.douzkj.zjjt.repository.dao.Signal;
import com.douzkj.zjjt.repository.entity.SignalConfig;
import com.douzkj.zjjt.web.convertor.SignalConvertor;
import com.douzkj.zjjt.web.param.SignalCreateParam;
import com.douzkj.zjjt.web.param.SignalUpdateParam;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

/** 无 Spring 上下文、数据库或 MQ 的配置兼容测试。 */
class SignalVideoConfigTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();
    static final String BASE_CONFIG = "\"frame\":{\"storage\":{\"frameStoragePath\":\"/data/frames\"}},"
            + "\"algo\":{\"label\":{\"enabled\":true}}";

    static String config(String video) {
        return "{" + BASE_CONFIG + (video == null ? "" : ",\"video\":" + video) + "}";
    }

    static String request(String video) {
        return "{\"id\":24,\"name\":\"录像通路\",\"type\":\"SPECIAL\",\"config\":" + config(video) + "}";
    }

    @AfterAll
    static void closeValidator() {
        FACTORY.close();
    }

    @Test
    void oldConfigDefaultsToDisabledForJacksonAndHutool() throws Exception {
        assertDefaults(MAPPER.readValue(config(null), SignalConfig.class));
        assertDefaults(JSONUtil.toBean(config(null), SignalConfig.class));
    }

    @Test
    void omittedVideoFieldsUseDefaultsAndDoNotShareState() throws Exception {
        for (String video : new String[]{"{}", "{\"path\":\"/custom/video\"}"}) {
            assertDefaults(MAPPER.readValue(config(video), SignalConfig.class));
            assertDefaults(JSONUtil.toBean(config(video), SignalConfig.class));
        }
        SignalConfig first = MAPPER.readValue(config("{\"enabled\":true,\"durationSeconds\":120}"), SignalConfig.class);
        SignalConfig second = MAPPER.readValue(config(null), SignalConfig.class);
        assertTrue(MAPPER.valueToTree(first).path("video").path("enabled").asBoolean());
        assertDefaults(second);
    }

    @Test
    void createAndUpdateRoundTripPreserveVideoAndExistingSettings() throws Exception {
        String video = "{\"enabled\":true,\"durationSeconds\":120,\"path\":\"/custom/video\"}";
        SignalCreateParam create = MAPPER.readValue("{\"name\":\"录像通路\",\"config\":" + config(video) + "}", SignalCreateParam.class);
        SignalUpdateParam update = MAPPER.readValue(request(video), SignalUpdateParam.class);
        assertTrue(VALIDATOR.validate(create).isEmpty());
        assertTrue(VALIDATOR.validate(update).isEmpty());
        for (Signal stored : new Signal[]{SignalConvertor.INSTANCE.create2Do(create), SignalConvertor.INSTANCE.update2Do(update)}) {
            JsonNode saved = MAPPER.readTree(stored.getConfig());
            JsonNode returned = MAPPER.valueToTree(SignalConvertor.INSTANCE.do2Vo(stored).getConfig());
            assertEquals(true, saved.path("video").path("enabled").asBoolean());
            assertEquals(120, saved.path("video").path("durationSeconds").asInt());
            assertEquals("/custom/video", saved.path("video").path("path").asText());
            assertEquals(saved, returned);
            assertEquals("/data/frames", returned.path("frame").path("storage").path("frameStoragePath").asText());
            assertTrue(returned.path("algo").path("label").path("enabled").asBoolean());
        }
    }

    @Test
    void rejectsNullAndNonPositiveValuesThroughNestedValidation() throws Exception {
        for (String video : new String[]{"null", "{\"enabled\":null}", "{\"durationSeconds\":null}",
                "{\"durationSeconds\":0}", "{\"durationSeconds\":-1}"}) {
            SignalCreateParam param = MAPPER.readValue("{\"name\":\"test\",\"config\":" + config(video) + "}", SignalCreateParam.class);
            assertFalse(VALIDATOR.validate(param).isEmpty(), "应拒绝 " + video);
        }
    }

    @Test
    void rejectsFractionalAndOverflowDurationInsteadOfTruncating() {
        for (String duration : new String[]{"1.5", "60.0", "2147483648", "\"60\""}) {
            assertThrows(Exception.class, () -> MAPPER.readValue(config("{\"durationSeconds\":" + duration + "}"), SignalConfig.class));
        }
    }

    @Test
    void acceptsPositiveIntegerEvenWhenDisabled() throws Exception {
        for (int duration : new int[]{1, 60, 120, 3600}) {
            SignalCreateParam param = MAPPER.readValue("{\"name\":\"test\",\"config\":"
                    + config("{\"enabled\":false,\"durationSeconds\":" + duration + "}") + "}", SignalCreateParam.class);
            assertTrue(VALIDATOR.validate(param).isEmpty());
            assertEquals(duration, MAPPER.valueToTree(param.getConfig()).path("video").path("durationSeconds").asInt());
        }
    }

    private void assertDefaults(SignalConfig config) {
        JsonNode video = MAPPER.valueToTree(config).path("video");
        assertFalse(video.isMissingNode(), "旧通路也应返回 video 默认配置");
        assertFalse(video.path("enabled").asBoolean());
        assertEquals(60, video.path("durationSeconds").asInt());
        assertTrue(VALIDATOR.validate(config).isEmpty());
    }
}
