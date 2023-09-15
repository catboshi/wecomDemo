package tech.wedev.wecom.standard.impl;
import tech.wedev.wecom.standard.WelcomeMessageCfgService;
import tech.wedev.wecom.entity.po.WelcomeMessageCfgPO;
import tech.wedev.wecom.entity.qo.WelcomeMessageCfgQO;
import tech.wedev.wecom.mapper.BasicMapper;
import tech.wedev.wecom.mapper.WelcomeMessageCfgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WelcomeMessageCfgServiceImpl extends BasicServiceImpl<WelcomeMessageCfgPO,WelcomeMessageCfgQO> implements WelcomeMessageCfgService {

    @Autowired
    private WelcomeMessageCfgMapper welcomeMessageCfgMapper;
    @Override
    public BasicMapper<WelcomeMessageCfgPO,WelcomeMessageCfgQO> getBasicMapper(){
        return welcomeMessageCfgMapper;
    }
}