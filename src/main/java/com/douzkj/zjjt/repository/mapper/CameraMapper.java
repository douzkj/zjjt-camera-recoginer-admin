package com.douzkj.zjjt.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.douzkj.zjjt.repository.dao.Camera;
import com.douzkj.zjjt.repository.entity.SignalCameraCntVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author ranger dong
 * @date 22:29 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
public interface CameraMapper extends BaseMapper<Camera> {


    /**
     * 根据查询条件统计不同 signal_id 对应的 Camera 数量
     * @param wrapper 查询条件包装器
     * @return 以 signal_id 为键，对应数量为值的 Map
     */
    @Select("select signal_id, count(*) as `camera_cnt` from cameras ${ew.customSqlSegment} group by signal_id")
    List<SignalCameraCntVO> countBySignalId(@Param(Constants.WRAPPER) Wrapper<Camera> wrapper);
}
