package com.douzkj.zjjt.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.douzkj.zjjt.repository.dao.TaskDetail;
import com.douzkj.zjjt.repository.entity.TaskCollectCountVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TaskDetailMapper extends BaseMapper<TaskDetail> {


    @Select("SELECt signal_id, COUNT(frame_image_path) as frame_image_cnt, count(label_image_path) as label_image_cnt,  count(label_json_path) as label_json_cnt FROM task_detail ${ew.customSqlSegment}  group by signal_id")
    List<TaskCollectCountVO> countBySignalId(@Param(Constants.WRAPPER) Wrapper<TaskDetail> wrapper);


    @Select("SELECt task_id, COUNT(frame_image_path) as frame_image_cnt, count(label_image_path) as label_image_cnt,  count(label_json_path) as label_json_cnt FROM task_detail ${ew.customSqlSegment}  group by task_id")
    List<TaskCollectCountVO> countByTaskId(@Param(Constants.WRAPPER) Wrapper<TaskDetail> wrapper);
}
