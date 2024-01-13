package tech.wedev.wecom.mybatis.mapper;
import org.apache.ibatis.annotations.Param;
import tech.wedev.wecom.entity.po.CorpInfo;
import tech.wedev.wecom.entity.qo.CorpInfoQO;

import java.util.List;


public interface CorpInfoMapper extends BasicMapper<CorpInfo,CorpInfoQO> {
    List<CorpInfo> findByCorpId(@Param("corpId") String corpId);

    int updateByPrimaryKeySelective(CorpInfo record);
}