package tech.wedev.wecom.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import tech.wedev.wecom.standard.QywxContactConfigInfoService;
import tech.wedev.wecom.standard.BasicService;
import tech.wedev.wecom.entity.po.QywxContactConfigInfoPO;
import tech.wedev.wecom.entity.qo.QywxContactConfigInfoQO;


@RestController
@RequestMapping("zh_qywx_contact_config_info")
@Slf4j
public class QywxContactConfigInfoController extends BasicController<QywxContactConfigInfoPO, QywxContactConfigInfoQO> {
    @Autowired
    private QywxContactConfigInfoService qywxContactConfigInfoService;
    @Override
    public BasicService<QywxContactConfigInfoPO,QywxContactConfigInfoQO> getBasicService(){
        return qywxContactConfigInfoService;
    }
}