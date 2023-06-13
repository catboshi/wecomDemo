package tech.wedev.wecom.dao;

import org.apache.ibatis.annotations.Param;
import tech.wedev.wecom.entity.po.ZhCustMgrMapPO;
import tech.wedev.wecom.entity.qo.ZhCustMgrMapQO;


public interface ZhCustMgrMapMapper extends BasicMapper<ZhCustMgrMapPO,ZhCustMgrMapQO> {
    ZhCustMgrMapPO selectByQywxMgrIdAndQywxCorpId(@Param("qywxMgrId") String qywxMgrId, @Param("qywxCorpId") String qywxCorpId);
}