package tech.wedev.wecom.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import tech.wedev.wecom.standard.OrgService;
import tech.wedev.wecom.standard.BasicService;
import tech.wedev.wecom.entity.po.OrgPO;
import tech.wedev.wecom.entity.qo.OrgQO;


@RestController
@RequestMapping("zh_org")
@Slf4j
public class OrgController extends BasicController<OrgPO, OrgQO> {
    @Autowired
    private OrgService orgService;
    @Override
    public BasicService<OrgPO,OrgQO> getBasicService(){
        return orgService;
    }
}