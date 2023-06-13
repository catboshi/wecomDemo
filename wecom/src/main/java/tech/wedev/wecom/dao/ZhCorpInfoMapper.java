package tech.wedev.wecom.dao;
import org.apache.ibatis.annotations.Param;
import tech.wedev.wecom.entity.po.ZhCorpInfo;
import tech.wedev.wecom.entity.qo.ZhCorpInfoQO;

import java.util.List;


public interface ZhCorpInfoMapper extends BasicMapper<ZhCorpInfo,ZhCorpInfoQO> {
    List<ZhCorpInfo> findByCorpId(@Param("corpId") String corpId);

    int updateByPrimaryKeySelective(ZhCorpInfo record);
}