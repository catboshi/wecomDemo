package tech.wedev.wecom.standard;
import tech.wedev.wecom.entity.po.ZhWecomMarketArticlePO;
import tech.wedev.wecom.entity.qo.ClientShareUploadQO;
import tech.wedev.wecom.entity.qo.ZhWecomMarketArticleQO;
import tech.wedev.wecom.entity.vo.ResponseVO;


public interface ZhWecomMarketArticleService extends BasicService<ZhWecomMarketArticlePO,ZhWecomMarketArticleQO> {
    ResponseVO<?> uploadQiweArticle(ClientShareUploadQO clientShareUploadQO);
}