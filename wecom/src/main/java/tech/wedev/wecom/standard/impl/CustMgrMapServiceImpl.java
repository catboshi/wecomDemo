package tech.wedev.wecom.standard.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.wedev.wecom.mapper.BasicMapper;
import tech.wedev.wecom.mapper.CustMgrMapMapper;
import tech.wedev.wecom.entity.po.CustMgrMapPO;
import tech.wedev.wecom.entity.qo.CustMgrMapQO;
import tech.wedev.wecom.standard.CustMgrMapService;


@Service
public class CustMgrMapServiceImpl extends BasicServiceImpl<CustMgrMapPO,CustMgrMapQO> implements CustMgrMapService {

    @Autowired
    private CustMgrMapMapper custMgrMapMapper;
    @Override
    public BasicMapper<CustMgrMapPO,CustMgrMapQO> getBasicMapper(){
        return custMgrMapMapper;
    }
}