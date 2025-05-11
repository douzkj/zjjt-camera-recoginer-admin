package com.douzkj.zjjt.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douzkj.zjjt.common.SignalConst;
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


    /**
     * 获取开启状态的通路
     *
     * @return
     */
    public List<Signal> getOpenedSignals() {
        return this.list(Wrappers.<Signal>lambdaQuery().eq(Signal::getStatus, SignalConst.SIGNAL_OPENED));
    }


    /**
     * 获取开启但是需要关闭的通路
     * @return
     */
    public List<Signal> getNeedAutoClosedSignals() {
        return this.list(Wrappers.<Signal>lambdaQuery()
                .eq(Signal::getStatus, SignalConst.SIGNAL_OPENED)
                .isNotNull(Signal::getClosedAtMs)
                .ne(Signal::getClosedAtMs, 0)
                .lt(Signal::getClosedAtMs, System.currentTimeMillis())
        );
    }
}
