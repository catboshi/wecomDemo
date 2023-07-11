package tech.wedev.wecom.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import tech.wedev.wecom.standard.CorpInfoService;
import tech.wedev.wecom.standard.BasicService;
import tech.wedev.wecom.entity.po.CorpInfo;
import tech.wedev.wecom.entity.qo.CorpInfoQO;


@RestController
@RequestMapping("zh_corp_info")
@Slf4j
public class CorpInfoController extends BasicController<CorpInfo, CorpInfoQO> {
    @Autowired
    private CorpInfoService corpInfoService;
    @Override
    public BasicService<CorpInfo,CorpInfoQO> getBasicService(){
        return corpInfoService;
    }
}