package tech.wedev.wecom.dao;
import org.apache.ibatis.annotations.Param;
import tech.wedev.wecom.entity.po.ZhOrgPO;
import tech.wedev.wecom.entity.qo.ZhOrgQO;
import tech.wedev.wecom.dao.BasicMapper;

import java.util.List;


public interface ZhOrgMapper extends BasicMapper<ZhOrgPO,ZhOrgQO> {
    List<ZhOrgPO> selectParentNodeInfoByCode(@Param("orgCode") String orgCode);
}