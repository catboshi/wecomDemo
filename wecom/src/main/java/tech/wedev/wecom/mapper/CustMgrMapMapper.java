package tech.wedev.wecom.mapper;

import org.apache.ibatis.annotations.Param;
import tech.wedev.wecom.entity.po.CustMgrMapPO;
import tech.wedev.wecom.entity.qo.CustMgrMapQO;


public interface CustMgrMapMapper extends BasicMapper<CustMgrMapPO, CustMgrMapQO> {
    CustMgrMapPO selectByQywxMgrIdAndQywxCorpId(@Param("qywxMgrId") String qywxMgrId, @Param("qywxCorpId") String qywxCorpId);
}