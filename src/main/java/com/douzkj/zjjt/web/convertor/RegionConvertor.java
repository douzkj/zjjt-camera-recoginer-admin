package com.douzkj.zjjt.web.convertor;

import com.douzkj.zjjt.repository.dao.CameraRegion;
import com.douzkj.zjjt.web.vo.RegionVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author ranger dong
 * @date 00:11 2025/4/3
 * @descrption
 * @copyright Spotter.ink
 */
@Mapper
public interface RegionConvertor {

    RegionConvertor INSTANCE = Mappers.getMapper(RegionConvertor.class);

    @Mappings(
            {@Mapping(target = "indexCode", source = "regionIndexCode"),
            @Mapping(target = "name", source = "regionName"),
            @Mapping(target = "pathName", source = "regionPathName"),
            @Mapping(target = "path", source = "regionPath")}
    )
    RegionVO do2Vo(CameraRegion region);


    List<RegionVO> do2Vo(List<CameraRegion> regions);
}
