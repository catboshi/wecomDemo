package tech.wedev.wecom.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import tech.wedev.wecom.standard.ZhWelcomeMessageCfgService;
import tech.wedev.wecom.standard.BasicService;
import tech.wedev.wecom.entity.po . ZhWelcomeMessageCfgPO;
import tech.wedev.wecom.entity.qo.ZhWelcomeMessageCfgQO;


@RestController
@RequestMapping("zh_zh_welcome_message_cfg")
@Slf4j
public class ZhWelcomeMessageCfgController extends BasicController<ZhWelcomeMessageCfgPO, ZhWelcomeMessageCfgQO> {
    @Autowired
    private ZhWelcomeMessageCfgService zhWelcomeMessageCfgService;
    @Override
    public BasicService<ZhWelcomeMessageCfgPO,ZhWelcomeMessageCfgQO> getBasicService(){
        return zhWelcomeMessageCfgService;
    }
}