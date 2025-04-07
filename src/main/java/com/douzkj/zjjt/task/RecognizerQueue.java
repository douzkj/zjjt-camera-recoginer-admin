package com.douzkj.zjjt.task;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Data
@Component
@Slf4j
public class RecognizerQueue {

    private final RabbitTemplate rabbitTemplate;

    // 识别任务队列
    public static final String RECOGNIZER_QUEUE = "zjjt:camera_recognizer:task";


    public void deliver(RecognizerTask recognizerTask) {
        log.info("发送识别任务到队列:{}", JSONUtil.toJsonStr(recognizerTask));
        rabbitTemplate.convertAndSend(RECOGNIZER_QUEUE, recognizerTask);
    }
}
