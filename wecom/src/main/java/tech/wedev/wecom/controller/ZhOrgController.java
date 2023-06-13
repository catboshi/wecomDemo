package tech.wedev.wecom.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import tech.wedev.wecom.standard.ZhOrgService;
import tech.wedev.wecom.standard.BasicService;
import tech.wedev.wecom.entity.po . ZhOrgPO;
import tech.wedev.wecom.entity.qo.ZhOrgQO;


@RestController
@RequestMapping("zh_zh_org")
@Slf4j
public class ZhOrgController extends BasicController<ZhOrgPO, ZhOrgQO> {
    @Autowired
    private ZhOrgService zhOrgService;
    @Override
    public BasicService<ZhOrgPO,ZhOrgQO> getBasicService(){
        return zhOrgService;
    }
}