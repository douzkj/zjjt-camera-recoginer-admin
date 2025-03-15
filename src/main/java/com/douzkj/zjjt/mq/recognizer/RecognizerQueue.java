package com.douzkj.zjjt.mq.recognizer;

import lombok.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Data
@Component
public class RecognizerQueue {

    private final RabbitTemplate rabbitTemplate;

    // 识别任务队列
    public static final String RECOGNIZER_QUEUE = "zjjt:recognizer:task";


    public void deliver(RecognizerTask recognizerTask) {
        rabbitTemplate.convertAndSend(RECOGNIZER_QUEUE, recognizerTask);
    }
}
