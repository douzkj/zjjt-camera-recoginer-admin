package com.douzkj.zjjt.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.douzkj.zjjt.common.SysConfigKeys;
import com.douzkj.zjjt.repository.SysConfigRepository;
import com.douzkj.zjjt.repository.dao.SysConfig;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;


/**
 * @author ranger dong
 * @date 23:05 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@Service
@Data
public class SysConfigService {

    private final SysConfigRepository sysConfigRepository;


    private String getConfigValue(String key) {
        return getConfigValue(key, null);
    }


    private String getConfigValue(String key, String def) {
        SysConfig one = sysConfigRepository.getOne(Wrappers.<SysConfig>lambdaQuery()
                        .eq(SysConfig::getCfKey, key)
                , false);
        return (one != null && one.getCfValue() != null) ?
                one.getCfValue():
            def;
    }


    /**
     * 获取通道采集并行度
     */
    public int getCaptureParallelism() {
        return NumberUtils.toInt(getConfigValue(SysConfigKeys.CAPTURE_PARALLELISM), 10);
    }

    /**
     * 获取采集周期
     * 单位：秒
     */
    public long getCaptureIntervalSeconds() {
        return NumberUtils.toInt(getConfigValue(SysConfigKeys.CAPTURE_INTERVAL_SECONDS),    5 * 60);
    }
}
