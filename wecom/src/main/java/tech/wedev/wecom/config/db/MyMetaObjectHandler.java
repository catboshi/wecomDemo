package tech.wedev.wecom.config.db;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("-------进入insertFill方法-------");
        this.setFieldValByName("isDelete", false, metaObject);
        this.setFieldValByName("gmtCreate", new Date(), metaObject);
        this.setFieldValByName("createId", 555114100L, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("-------进入updateFill方法-------");
        this.setFieldValByName("gmtModified", new Date(), metaObject);
        this.setFieldValByName("modifiedId", 555114100L, metaObject);
    }
}
