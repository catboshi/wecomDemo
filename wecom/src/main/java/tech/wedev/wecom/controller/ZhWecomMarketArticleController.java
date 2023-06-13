package tech.wedev.wecom.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import tech.wedev.wecom.standard.ZhWecomMarketArticleService;
import tech.wedev.wecom.standard.BasicService;
import tech.wedev.wecom.entity.po . ZhWecomMarketArticlePO;
import tech.wedev.wecom.entity.qo.ZhWecomMarketArticleQO;


@RestController
@RequestMapping("zh_zh_wecom_market_article")
@Slf4j
public class ZhWecomMarketArticleController extends BasicController<ZhWecomMarketArticlePO, ZhWecomMarketArticleQO> {
    @Autowired
    private ZhWecomMarketArticleService zhWecomMarketArticleService;
    @Override
    public BasicService<ZhWecomMarketArticlePO,ZhWecomMarketArticleQO> getBasicService(){
        return zhWecomMarketArticleService;
    }
}