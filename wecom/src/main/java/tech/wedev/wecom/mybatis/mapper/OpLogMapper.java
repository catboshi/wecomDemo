package tech.wedev.wecom.mybatis.mapper;

import tech.wedev.wecom.entity.po.OpLogPO;

public interface OpLogMapper {
    Integer saveLog(OpLogPO opLogPO);
}
