package com.douzkj.zjjt.web.convertor;

import com.douzkj.zjjt.repository.dao.Signal;
import com.douzkj.zjjt.repository.dao.SignalCameraCount;
import com.douzkj.zjjt.web.param.SignalCreateParam;
import com.douzkj.zjjt.web.vo.SignalVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author ranger dong
 * @date 21:53 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@Mapper
public interface SignalConvertor {
    SignalConvertor INSTANCE = Mappers.getMapper(SignalConvertor.class);


    List<SignalVO> do2CameraVo(List<SignalCameraCount> signalCameraCounts);


    SignalVO do2Vo(Signal signal);



    Signal create2Do(SignalCreateParam signalCreateParam);
    Signal update2Do(SignalCreateParam signalCreateParam);
}
