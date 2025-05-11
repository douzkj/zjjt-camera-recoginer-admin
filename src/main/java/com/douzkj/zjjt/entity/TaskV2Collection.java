package com.douzkj.zjjt.entity;

import com.douzkj.zjjt.task.RecognizerTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskV2Collection implements Serializable {

    private String taskId;

    private RecognizerTask.CameraDTO camera;

    private RecognizerTask.SignalDTO signal;

    private Map<String, Object> collect;

    private Long timestamp;
}
