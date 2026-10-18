package com.douzkj.zjjt;

import com.douzkj.zjjt.repository.SignalRepository;
import com.douzkj.zjjt.repository.CameraRepository;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import com.douzkj.zjjt.repository.dao.Signal;
import com.douzkj.zjjt.repository.dao.SignalCameraCount;
import com.douzkj.zjjt.web.SignalController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** 真实控制器、转换器、参数校验，仅隔离持久化层。 */
class SignalVideoConfigApiTest {
    private SignalRepository repository;
    private CameraRepository cameras;
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        // 隔离测试不启动 MyBatis，但分页控制器的 lambda 排序需要实体元数据。
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), "video-config-test"), Signal.class);
        repository = mock(SignalRepository.class);
        cameras = mock(CameraRepository.class);
        SignalController controller = new SignalController();
        ReflectionTestUtils.setField(controller, "signalRepository", repository);
        ReflectionTestUtils.setField(controller, "cameraRepository", cameras);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createReturnsAndStoresDefaultsForOldClients() throws Exception {
        mvc.perform(post("/signal/create").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"old\",\"config\":" + SignalVideoConfigTest.config(null) + "}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.config.video.enabled").value(false))
                .andExpect(jsonPath("$.data.config.video.durationSeconds").value(60));
        ArgumentCaptor<Signal> signal = ArgumentCaptor.forClass(Signal.class);
        verify(repository).save(signal.capture());
        assertTrue(signal.getValue().getConfig().contains("\"durationSeconds\":60"));
    }

    @Test
    void updateReturnsVideoAndPreservesPath() throws Exception {
        mvc.perform(post("/signal/update").contentType(MediaType.APPLICATION_JSON)
                        .content(SignalVideoConfigTest.request("{\"enabled\":true,\"durationSeconds\":90,\"path\":\"/custom/video\"}")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.config.video.enabled").value(true))
                .andExpect(jsonPath("$.data.config.video.durationSeconds").value(90))
                .andExpect(jsonPath("$.data.config.video.path").value("/custom/video"));
        verify(repository).updateById(any(Signal.class));
    }

    @Test
    void listReturnsBothOldDefaultsAndSavedVideo() throws Exception {
        SignalCameraCount old = new SignalCameraCount();
        old.setConfig(SignalVideoConfigTest.config(null));
        when(repository.listCameraCnt()).thenReturn(Collections.singletonList(old));
        mvc.perform(get("/signal/list")).andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].config.video.enabled").value(false))
                .andExpect(jsonPath("$.data[0].config.video.durationSeconds").value(60));
        old.setConfig(SignalVideoConfigTest.config("{\"enabled\":true,\"durationSeconds\":120}"));
        mvc.perform(get("/signal/list")).andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].config.video.enabled").value(true))
                .andExpect(jsonPath("$.data[0].config.video.durationSeconds").value(120));
    }

    @Test
    void pageReturnsSavedVideoConfiguration() throws Exception {
        Signal signal = new Signal();
        signal.setId(24L);
        signal.setConfig(SignalVideoConfigTest.config("{\"enabled\":true,\"durationSeconds\":90,\"path\":\"/custom/video\"}"));
        Page<Signal> page = new Page<>(1, 10);
        page.setRecords(Collections.singletonList(signal));
        page.setTotal(1);
        when(repository.page(any(Page.class), any())).thenReturn(page);
        when(cameras.countBySignalIds(any())).thenReturn(Collections.emptyMap());
        mvc.perform(get("/signal/page").param("current", "1").param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items[0].config.video.enabled").value(true))
                .andExpect(jsonPath("$.data.items[0].config.video.durationSeconds").value(90))
                .andExpect(jsonPath("$.data.items[0].config.video.path").value("/custom/video"));
    }

    @Test
    void rejectsBadCreateAndUpdateWithoutWriting() throws Exception {
        for (String endpoint : new String[]{"/signal/create", "/signal/update"}) {
            for (String video : new String[]{"null", "{\"enabled\":null}", "{\"durationSeconds\":null}",
                    "{\"durationSeconds\":0}", "{\"durationSeconds\":-1}", "{\"durationSeconds\":1.5}"}) {
                String body = endpoint.endsWith("update") ? SignalVideoConfigTest.request(video)
                        : "{\"name\":\"test\",\"config\":" + SignalVideoConfigTest.config(video) + "}";
                mvc.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(body))
                        .andExpect(status().isBadRequest());
            }
        }
        verifyNoInteractions(repository);
    }
}
