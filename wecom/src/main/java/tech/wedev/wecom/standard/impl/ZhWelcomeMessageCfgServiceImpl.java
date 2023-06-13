package tech.wedev.wecom.standard.impl;
import tech.wedev.wecom.standard.ZhWelcomeMessageCfgService;
import tech.wedev.wecom.entity.po.ZhWelcomeMessageCfgPO;
import tech.wedev.wecom.entity.qo.ZhWelcomeMessageCfgQO;
import tech.wedev.wecom.dao.BasicMapper;
import tech.wedev.wecom.dao.ZhWelcomeMessageCfgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ZhWelcomeMessageCfgServiceImpl extends BasicServiceImpl<ZhWelcomeMessageCfgPO,ZhWelcomeMessageCfgQO> implements ZhWelcomeMessageCfgService {

    @Autowired
    private ZhWelcomeMessageCfgMapper zhWelcomeMessageCfgMapper;
    @Override
    public BasicMapper<ZhWelcomeMessageCfgPO,ZhWelcomeMessageCfgQO> getBasicMapper(){
        return zhWelcomeMessageCfgMapper;
    }
}