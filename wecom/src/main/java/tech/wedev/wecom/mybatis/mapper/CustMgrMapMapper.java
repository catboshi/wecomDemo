package tech.wedev.wecom.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import tech.wedev.wecom.entity.po.CustMgrMapPO;
import tech.wedev.wecom.entity.qo.CustMgrMapQO;

import java.util.List;


public interface CustMgrMapMapper extends BasicMapper<CustMgrMapPO, CustMgrMapQO> {
    CustMgrMapPO selectByQywxMgrIdAndQywxCorpId(@Param("qywxMgrId") String qywxMgrId, @Param("qywxCorpId") String qywxCorpId);

    List<CustMgrMapPO> selectList();
}