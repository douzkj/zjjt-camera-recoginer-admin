package com.douzkj.zjjt.web.convertor;

import com.douzkj.zjjt.repository.dao.Task;
import com.douzkj.zjjt.repository.dao.TaskDetail;
import com.douzkj.zjjt.utils.ConvertorUtil;
import com.douzkj.zjjt.web.vo.TaskDetailVO;
import com.douzkj.zjjt.web.vo.TaskVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper(uses = {ConvertorUtil.class})
public interface TaskConvertor {
    TaskConvertor INSTANCE = Mappers.getMapper(TaskConvertor.class);

    TaskVO do2Vo(Task task);


    TaskDetailVO do2Vo(TaskDetail taskDetail);

}
