package com.douzkj.zjjt.repository.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("task_export")
public class TaskExport extends Base {
    private String exportId;
    private Long startedAtMs;
    private Long finishedAtMs;
    private Integer status;
    private String filepath;
}
