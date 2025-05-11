package com.douzkj.zjjt.web;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.douzkj.zjjt.entity.R;
import com.douzkj.zjjt.repository.SysConfigRepository;
import com.douzkj.zjjt.repository.dao.SysConfig;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调度配置
 *
 * @author ranger dong
 * @date 22:40 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@RestController
@RequestMapping("/sys-config")
@Data
public class SysConfigController {

    private final SysConfigRepository sysConfigRepository;

    @PostMapping("/save")
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> save(@RequestBody Map<String, String> values) {
        for (Map.Entry<String, String> stringObjectEntry : values.entrySet()) {
            String key = stringObjectEntry.getKey();
            String value = stringObjectEntry.getValue();
            SysConfig sysConfig = new SysConfig();
            sysConfig.setCfKey(key);
            sysConfig.setCfValue(value);
            sysConfigRepository.saveOrUpdate(sysConfig,
                    Wrappers.<SysConfig>lambdaQuery().eq(SysConfig::getCfKey, key)
            );
        }
        return R.success();
    }


    @GetMapping("/")
    public R<Map<String, String>> getSysConfig() {
        List<SysConfig> list = sysConfigRepository.list();
        Map<String, String> res = new HashMap<>();
        for (SysConfig sysConfig : list) {
            res.put(sysConfig.getCfKey(), sysConfig.getCfValue());
        }
        return R.success(res);
    }


}
