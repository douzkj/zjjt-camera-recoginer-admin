package com.douzkj.zjjt.infra.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GmtMetaObjectHandler implements MetaObjectHandler {

    public static final String FIELD_CREATE = "createdAt";
    public static final String FIELD_UPDATE = "updatedAt";

    @Override
    public void insertFill(MetaObject metaObject) {
        if (this.checkHasSetterAndNonVal(FIELD_CREATE, metaObject)) {
            this.strictInsertFill(metaObject, FIELD_CREATE, LocalDateTime.class, LocalDateTime.now());
        }
        if (this.checkHasSetterAndNonVal(FIELD_UPDATE, metaObject)) {
            this.strictInsertFill(metaObject, FIELD_UPDATE, LocalDateTime.class, LocalDateTime.now());
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter(FIELD_UPDATE)) {
            this.setFieldValByName(FIELD_UPDATE, LocalDateTime.now(), metaObject);
        }
    }

    /**
     * 判断属性包含和已设置值
     *
     * @param fieldName 字段名
     * @param metaObject object
     * @return boolean
     */
    public boolean checkHasSetterAndNonVal(String fieldName, MetaObject metaObject) {
        boolean setter = metaObject.hasSetter(fieldName);
        Object val = getFieldValByName(fieldName, metaObject);
        return  setter && (val == null);
    }
}
