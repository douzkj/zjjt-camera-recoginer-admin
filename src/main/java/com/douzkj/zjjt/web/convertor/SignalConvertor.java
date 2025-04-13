package com.douzkj.zjjt.web.convertor;

import com.douzkj.zjjt.repository.dao.Signal;
import com.douzkj.zjjt.repository.dao.SignalCameraCount;
import com.douzkj.zjjt.utils.ConvertorUtil;
import com.douzkj.zjjt.web.param.SignalCreateParam;
import com.douzkj.zjjt.web.param.SignalUpdateParam;
import com.douzkj.zjjt.web.vo.SignalVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author ranger dong
 * @date 21:53 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@Mapper(uses = {ConvertorUtil.class})
public interface SignalConvertor {
    SignalConvertor INSTANCE = Mappers.getMapper(SignalConvertor.class);


    List<SignalVO> do2CameraVo(List<SignalCameraCount> signalCameraCounts);

    @Mapping(target = "config", expression = "java(cn.hutool.json.JSONUtil.toBean(signal.getConfig(), com.douzkj.zjjt.repository.entity.SignalConfig.class))")
    SignalVO do2Vo(Signal signal);



    @Mapping(target = "config", source = "config", qualifiedByName = "objToJson")
    Signal create2Do(SignalCreateParam param);

    @Mapping(target = "config", source = "config", qualifiedByName = "objToJson")
    Signal update2Do(SignalUpdateParam param);
}
