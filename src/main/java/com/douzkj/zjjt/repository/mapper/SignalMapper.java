package com.douzkj.zjjt.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.douzkj.zjjt.repository.dao.Signal;
import com.douzkj.zjjt.repository.dao.SignalCameraCount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ranger dong
 * @date 21:39 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
public interface SignalMapper extends BaseMapper<Signal> {

    List<SignalCameraCount> listCameraCnt();

}
