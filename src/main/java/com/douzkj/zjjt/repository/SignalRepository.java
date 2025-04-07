package com.douzkj.zjjt.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douzkj.zjjt.repository.dao.Signal;
import com.douzkj.zjjt.repository.dao.SignalCameraCount;
import com.douzkj.zjjt.repository.mapper.SignalMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ranger dong
 * @date 22:15 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@Repository
public class SignalRepository extends ServiceImpl<SignalMapper, Signal> {
    public List<SignalCameraCount> listCameraCnt() {
        return baseMapper.listCameraCnt();
    }

    @Override
    public boolean save(Signal entity) {
        if (entity.getId() != null) {
            return updateById(entity);
        }
        return super.save(entity);
    }
}
