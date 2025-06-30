package com.douzkj.zjjt.web.convertor;


import com.douzkj.zjjt.repository.dao.Target;
import com.douzkj.zjjt.utils.ConvertorUtil;
import com.douzkj.zjjt.web.vo.TargetLabelVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {ConvertorUtil.class})
public interface TargetConvertor {

    TargetConvertor INSTANCE = Mappers.getMapper(TargetConvertor.class);


    List<TargetLabelVO> do2LabelVO(List<Target> targets);
}
