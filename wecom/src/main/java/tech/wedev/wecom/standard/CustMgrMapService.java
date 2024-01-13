package tech.wedev.wecom.standard;
import tech.wedev.wecom.entity.po.CustMgrMapPO;
import tech.wedev.wecom.entity.qo.CustMgrMapQO;

import java.util.List;


public interface CustMgrMapService extends BasicService<CustMgrMapPO,CustMgrMapQO> {
    List<CustMgrMapPO> selectList();
}