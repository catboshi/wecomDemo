package tech.wedev.wecom.dao;

import tech.wedev.wecom.entity.po.WelcomeMessageCfgPO;
import tech.wedev.wecom.entity.qo.WelcomeMessageCfgQO;


public interface WelcomeMessageCfgMapper extends BasicMapper<WelcomeMessageCfgPO,WelcomeMessageCfgQO> {
    WelcomeMessageCfgPO selectByPrimaryKey(Long id);

    WelcomeMessageCfgPO selectDefaultWelcomMessage(String source, String corpId, String parentCode);
}