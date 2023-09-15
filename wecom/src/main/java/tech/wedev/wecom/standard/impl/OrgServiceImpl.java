package tech.wedev.wecom.standard.impl;
import tech.wedev.wecom.standard.OrgService;
import tech.wedev.wecom.entity.po.OrgPO;
import tech.wedev.wecom.entity.qo.OrgQO;
import tech.wedev.wecom.mapper.BasicMapper;
import tech.wedev.wecom.mapper.OrgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrgServiceImpl extends BasicServiceImpl<OrgPO,OrgQO> implements OrgService {

    @Autowired
    private OrgMapper orgMapper;
    @Override
    public BasicMapper<OrgPO,OrgQO> getBasicMapper(){
        return orgMapper;
    }
}