package tech.wedev.wecom.dao;

import tech.wedev.wecom.entity.po.OpLogPO;

public interface OpLogMapper {
    Integer saveLog(OpLogPO opLogPO);
}
