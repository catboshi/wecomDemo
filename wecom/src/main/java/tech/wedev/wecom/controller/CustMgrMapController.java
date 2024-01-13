package tech.wedev.wecom.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import tech.wedev.wecom.standard.CustMgrMapService;
import tech.wedev.wecom.standard.BasicService;
import tech.wedev.wecom.entity.po . CustMgrMapPO;
import tech.wedev.wecom.entity.qo.CustMgrMapQO;


@RestController
@RequestMapping("zh_cust_mgr_map")
@Slf4j
public class CustMgrMapController extends BasicController<CustMgrMapPO, CustMgrMapQO> {

    @Autowired
    private CustMgrMapService custMgrMapService;

    @Override
    public BasicService<CustMgrMapPO,CustMgrMapQO> getBasicService(){
        return custMgrMapService;
    }
}