package com.douzkj.zjjt;

import com.douzkj.zjjt.repository.CameraRepository;
import com.douzkj.zjjt.repository.SignalRepository;
import com.douzkj.zjjt.repository.dao.Camera;
import com.douzkj.zjjt.service.CameraCaptureService;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = VideoRecognizerApplication.class)
public class RecognizerQueueTest {


    @Autowired
    private CameraCaptureService cameraCaptureService;

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private SignalRepository signalRepository;

    @Test
    public void testDeliver() {
        Camera camera = cameraRepository.getById(851L);
        CameraCaptureService.SignalRunnerContext context = new CameraCaptureService.SignalRunnerContext(
                signalRepository.getById(1L), Lists.newArrayList(
                camera
                )
        );
        cameraCaptureService.executorSignal(context);
//        RecognizerTask recognizerTask = new RecognizerTask();
//        RecognizerQueue recognizerQueue = new RecognizerQueue(rabbitTemplate);
//        recognizerQueue.deliver(recognizerTask);
    }
}