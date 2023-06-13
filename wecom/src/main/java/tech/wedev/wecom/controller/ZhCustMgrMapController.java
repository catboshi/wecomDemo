package tech.wedev.wecom.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import tech.wedev.wecom.standard.ZhCustMgrMapService;
import tech.wedev.wecom.standard.BasicService;
import tech.wedev.wecom.entity.po . ZhCustMgrMapPO;
import tech.wedev.wecom.entity.qo.ZhCustMgrMapQO;


@RestController
@RequestMapping("zh_zh_cust_mgr_map")
@Slf4j
public class ZhCustMgrMapController extends BasicController<ZhCustMgrMapPO, ZhCustMgrMapQO> {
    @Autowired
    private ZhCustMgrMapService zhCustMgrMapService;
    @Override
    public BasicService<ZhCustMgrMapPO,ZhCustMgrMapQO> getBasicService(){
        return zhCustMgrMapService;
    }
}