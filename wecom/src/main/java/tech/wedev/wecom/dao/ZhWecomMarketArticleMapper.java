package tech.wedev.wecom.dao;
import org.apache.ibatis.annotations.Param;
import tech.wedev.wecom.entity.po.ZhWecomMarketArticlePO;
import tech.wedev.wecom.entity.qo.ClientShareUploadQO;
import tech.wedev.wecom.entity.qo.ZhWecomMarketArticleQO;
import tech.wedev.wecom.dao.BasicMapper;

import java.util.Date;


public interface ZhWecomMarketArticleMapper extends BasicMapper<ZhWecomMarketArticlePO,ZhWecomMarketArticleQO> {

    int updateByArticleSourceAndArticleApp(ClientShareUploadQO clientShareUploadQO, @Param("mediaId") String mediaId, @Param("date") Date date);
    ZhWecomMarketArticlePO checkIsValid(@Param("id") Long id);
}