package com.douzkj.zjjt.repository.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@TableName("tasks")
@NoArgsConstructor
@Data
public class Task extends Base {
    private String taskId;
    private Integer collImageCnt;
    private Integer collLabelImageCnt;
    private Integer collLabelJsonCnt;
    private Long signalId;
    private Integer status;
    private Long openedAtMs;
    private Long closedAtMs;
}
