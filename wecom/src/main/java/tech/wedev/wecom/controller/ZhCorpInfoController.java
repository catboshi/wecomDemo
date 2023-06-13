package tech.wedev.wecom.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import tech.wedev.wecom.standard.ZhCorpInfoService;
import tech.wedev.wecom.standard.BasicService;
import tech.wedev.wecom.entity.po.ZhCorpInfo;
import tech.wedev.wecom.entity.qo.ZhCorpInfoQO;


@RestController
@RequestMapping("zh_zh_corp_info")
@Slf4j
public class ZhCorpInfoController extends BasicController<ZhCorpInfo, ZhCorpInfoQO> {
    @Autowired
    private ZhCorpInfoService zhCorpInfoService;
    @Override
    public BasicService<ZhCorpInfo,ZhCorpInfoQO> getBasicService(){
        return zhCorpInfoService;
    }
}