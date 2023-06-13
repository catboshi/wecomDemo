package tech.wedev.wecom.standard.impl;
import tech.wedev.wecom.standard.ZhOrgService;
import tech.wedev.wecom.entity.po.ZhOrgPO;
import tech.wedev.wecom.entity.qo.ZhOrgQO;
import tech.wedev.wecom.dao.BasicMapper;
import tech.wedev.wecom.dao.ZhOrgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ZhOrgServiceImpl extends BasicServiceImpl<ZhOrgPO,ZhOrgQO> implements ZhOrgService {

    @Autowired
    private ZhOrgMapper zhOrgMapper;
    @Override
    public BasicMapper<ZhOrgPO,ZhOrgQO> getBasicMapper(){
        return zhOrgMapper;
    }
}