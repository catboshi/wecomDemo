package tech.wedev.wecom.dao;

import org.apache.ibatis.annotations.Param;
import tech.wedev.wecom.entity.po.WecomMarketArticlePO;
import tech.wedev.wecom.entity.qo.ClientShareUploadQO;
import tech.wedev.wecom.entity.qo.WecomMarketArticleQO;

import java.util.Date;


public interface WecomMarketArticleMapper extends BasicMapper<WecomMarketArticlePO,WecomMarketArticleQO> {
    WecomMarketArticlePO selectOneByArticleSource(@Param("articleSource") String articleSource);
    int updateByArticleSourceAndArticleApp(ClientShareUploadQO clientShareUploadQO, @Param("mediaId") String mediaId, @Param("date") Date date);
    WecomMarketArticlePO checkIsValid(@Param("id") Long id);

}