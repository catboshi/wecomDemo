package tech.wedev.wecom.dao;
import tech.wedev.wecom.entity.po.ZhWelcomeMessageCfgPO;
import tech.wedev.wecom.entity.qo.ZhWelcomeMessageCfgQO;
import tech.wedev.wecom.dao.BasicMapper;


public interface ZhWelcomeMessageCfgMapper extends BasicMapper<ZhWelcomeMessageCfgPO,ZhWelcomeMessageCfgQO> {
    ZhWelcomeMessageCfgPO selectByPrimaryKey(Long id);

    ZhWelcomeMessageCfgPO selectDefaultWelcomMessage(String source, String corpId, String parentCode);
}