package com.douzkj.zjjt.web.convertor;

import com.douzkj.zjjt.repository.dao.Camera;
import com.douzkj.zjjt.web.vo.CameraVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author ranger dong
 * @date 00:27 2025/3/24
 * @descrption
 * @copyright Spotter.ink
 */
@Mapper
public interface CameraConvertor {
    CameraConvertor INSTANCE = Mappers.getMapper(CameraConvertor.class);


    @Mappings(
            @Mapping(target = "hkMeta", expression = "java(cn.hutool.json.JSONUtil.toBean(camera.getHkMeta(), com.douzkj.zjjt.infra.hikvision.api.video.resource.entity.HikvisionCameraV2Model.class))")
    )
    CameraVO do2Vo(Camera camera);
}
