package tech.wedev.wecom.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import tech.wedev.wecom.standard.WecomMarketArticleService;
import tech.wedev.wecom.standard.BasicService;
import tech.wedev.wecom.entity.po.WecomMarketArticlePO;
import tech.wedev.wecom.entity.qo.WecomMarketArticleQO;


@RestController
@RequestMapping("zh_wecom_market_article")
@Slf4j
public class WecomMarketArticleController extends BasicController<WecomMarketArticlePO, WecomMarketArticleQO> {
    @Autowired
    private WecomMarketArticleService wecomMarketArticleService;
    @Override
    public BasicService<WecomMarketArticlePO,WecomMarketArticleQO> getBasicService(){
        return wecomMarketArticleService;
    }
}