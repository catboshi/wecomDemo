package tech.wedev.wecom.standard;
import tech.wedev.wecom.entity.po.WecomMarketArticlePO;
import tech.wedev.wecom.entity.qo.ClientShareUploadQO;
import tech.wedev.wecom.entity.qo.WecomMarketArticleQO;
import tech.wedev.wecom.entity.vo.ResponseVO;


public interface WecomMarketArticleService extends BasicService<WecomMarketArticlePO,WecomMarketArticleQO> {
    ResponseVO<?> uploadQiweArticle(ClientShareUploadQO clientShareUploadQO);
}