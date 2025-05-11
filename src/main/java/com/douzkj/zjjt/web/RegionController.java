package com.douzkj.zjjt.web;

import com.douzkj.zjjt.entity.R;
import com.douzkj.zjjt.repository.RegionRepository;
import com.douzkj.zjjt.repository.dao.CameraRegion;
import com.douzkj.zjjt.web.convertor.RegionConvertor;
import com.douzkj.zjjt.web.vo.RegionVO;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ranger dong
 * @date 00:22 2025/4/2
 * @descrption
 * @copyright Spotter.ink
 */
@RestController
@RequestMapping("/region")
@Data
public class RegionController {
    
    private final RegionRepository regionRepository;

    @GetMapping("/options")
    public R<List<RegionVO>> selectOptions() {
        List<CameraRegion> regions = regionRepository.list();
        List<RegionVO> regionVOS = RegionConvertor.INSTANCE.do2Vo(regions);
        return R.success(regionVOS);
    }
}
