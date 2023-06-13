package tech.wedev.wecom.standard.impl;
import tech.wedev.wecom.standard.ZhCustMgrMapService;
import tech.wedev.wecom.entity.po.ZhCustMgrMapPO;
import tech.wedev.wecom.entity.qo.ZhCustMgrMapQO;
import tech.wedev.wecom.dao.BasicMapper;
import tech.wedev.wecom.dao.ZhCustMgrMapMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ZhCustMgrMapServiceImpl extends BasicServiceImpl<ZhCustMgrMapPO,ZhCustMgrMapQO> implements ZhCustMgrMapService {

    @Autowired
    private ZhCustMgrMapMapper zhCustMgrMapMapper;
    @Override
    public BasicMapper<ZhCustMgrMapPO,ZhCustMgrMapQO> getBasicMapper(){
        return zhCustMgrMapMapper;
    }
}