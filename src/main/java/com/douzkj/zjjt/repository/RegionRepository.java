package com.douzkj.zjjt.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.douzkj.zjjt.repository.dao.Camera;
import com.douzkj.zjjt.repository.dao.CameraRegion;
import com.douzkj.zjjt.repository.mapper.CameraMapper;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ranger dong
 * @date 00:07 2025/4/3
 * @descrption
 * @copyright Spotter.ink
 */
@Repository
@Data
public class RegionRepository {

    private final CameraMapper cameraMapper;


    public List<CameraRegion> list() {
        List<Camera> cameras = cameraMapper.selectList(Wrappers.<Camera>query()
                .select("distinct region_name, region_path_name, region_index_code, region_path"));
        return cameras.stream().map(camera -> {
            CameraRegion region = new CameraRegion();
            region.setRegionName(camera.getRegionName());
            region.setRegionPathName(camera.getRegionPathName());
            region.setRegionIndexCode(camera.getRegionIndexCode());
            region.setRegionPath(camera.getRegionPath());
            return region;
        }).collect(Collectors.toList());
    }
}
