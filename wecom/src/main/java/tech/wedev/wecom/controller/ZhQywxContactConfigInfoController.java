package tech.wedev.wecom.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import tech.wedev.wecom.standard.ZhQywxContactConfigInfoService;
import tech.wedev.wecom.standard.BasicService;
import tech.wedev.wecom.entity.po . ZhQywxContactConfigInfoPO;
import tech.wedev.wecom.entity.qo.ZhQywxContactConfigInfoQO;


@RestController
@RequestMapping("zh_zh_qywx_contact_config_info")
@Slf4j
public class ZhQywxContactConfigInfoController extends BasicController<ZhQywxContactConfigInfoPO, ZhQywxContactConfigInfoQO> {
    @Autowired
    private ZhQywxContactConfigInfoService zhQywxContactConfigInfoService;
    @Override
    public BasicService<ZhQywxContactConfigInfoPO,ZhQywxContactConfigInfoQO> getBasicService(){
        return zhQywxContactConfigInfoService;
    }
}