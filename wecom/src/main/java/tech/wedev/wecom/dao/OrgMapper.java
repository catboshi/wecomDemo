package tech.wedev.wecom.dao;

import org.apache.ibatis.annotations.Param;
import tech.wedev.wecom.entity.po.OrgPO;
import tech.wedev.wecom.entity.qo.OrgQO;

import java.util.List;


public interface OrgMapper extends BasicMapper<OrgPO,OrgQO> {
    List<OrgPO> selectParentNodeInfoByCode(@Param("orgCode") String orgCode);
}