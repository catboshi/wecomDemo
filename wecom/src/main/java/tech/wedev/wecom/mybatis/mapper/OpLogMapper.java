package tech.wedev.wecom.mybatis.mapper;

import tech.wedev.wecom.entity.po.OpLogPO;

import java.util.List;
import java.util.Map;

public interface OpLogMapper {
    Integer saveLog(OpLogPO opLogPO);

    List<Map<String,Object>> selectList();

    Integer update(Map<String,Object> param);
}
