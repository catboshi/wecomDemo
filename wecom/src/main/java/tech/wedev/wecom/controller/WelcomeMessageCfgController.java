package tech.wedev.wecom.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import tech.wedev.wecom.standard.WelcomeMessageCfgService;
import tech.wedev.wecom.standard.BasicService;
import tech.wedev.wecom.entity.po.WelcomeMessageCfgPO;
import tech.wedev.wecom.entity.qo.WelcomeMessageCfgQO;


@RestController
@RequestMapping("zh_welcome_message_cfg")
@Slf4j
public class WelcomeMessageCfgController extends BasicController<WelcomeMessageCfgPO, WelcomeMessageCfgQO> {
    @Autowired
    private WelcomeMessageCfgService welcomeMessageCfgService;
    @Override
    public BasicService<WelcomeMessageCfgPO,WelcomeMessageCfgQO> getBasicService(){
        return welcomeMessageCfgService;
    }
}