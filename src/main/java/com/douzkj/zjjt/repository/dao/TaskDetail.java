package com.douzkj.zjjt.repository.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@TableName("task_detail")
@NoArgsConstructor
@Data
public class TaskDetail extends Base{
    private String cameraIndexCode;
    private String cameraName;
    private String cameraAddr;
    private String regionPathName;
    private String regionIndexCode;
    private String taskId;
    private Long signalId;
    private String frameImagePath;
    private Long frameTimeMs;
    private String labelImagePath;
    private String labelJsonPath;
    private String labelTypes;
    private Long labelTimeMs;
}
