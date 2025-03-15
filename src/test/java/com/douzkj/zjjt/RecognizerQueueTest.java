package com.douzkj.zjjt;

import com.douzkj.zjjt.mq.recognizer.RecognizerQueue;
import com.douzkj.zjjt.mq.recognizer.RecognizerTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.douzkj.zjjt.mq.recognizer.RecognizerQueue.RECOGNIZER_QUEUE;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = VideoRecognizerApplication.class)
public class RecognizerQueueTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testDeliver() {
        RecognizerTask recognizerTask = new RecognizerTask("xxxxx", "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov");
        RecognizerQueue recognizerQueue = new RecognizerQueue(rabbitTemplate);
        recognizerQueue.deliver(recognizerTask);
    }
}