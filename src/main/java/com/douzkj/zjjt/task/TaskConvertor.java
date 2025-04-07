package com.douzkj.zjjt.task;

import com.douzkj.zjjt.repository.dao.Camera;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author ranger dong
 * @date 02:38 2025/3/30
 * @descrption
 */
@Mapper
public interface TaskConvertor {

    TaskConvertor INSTANCE = Mappers.getMapper(TaskConvertor.class);

    @Mappings({
            @Mapping(source = "indexCode", target = "indexCode"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "hkMeta", target = "hkMeta"),
    })
    RecognizerTask.CameraDTO do2TaskDTO(Camera camera);
}
