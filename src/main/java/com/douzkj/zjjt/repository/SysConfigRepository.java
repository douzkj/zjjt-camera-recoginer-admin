package com.douzkj.zjjt.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douzkj.zjjt.repository.dao.SysConfig;
import com.douzkj.zjjt.repository.mapper.SysConfigMapper;
import org.springframework.stereotype.Repository;

/**
 * @author ranger dong
 * @date 22:58 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@Repository
public class SysConfigRepository extends ServiceImpl<SysConfigMapper, SysConfig> {


    @Override
    public boolean save(SysConfig entity) {
        return super.save(entity);
    }
}
