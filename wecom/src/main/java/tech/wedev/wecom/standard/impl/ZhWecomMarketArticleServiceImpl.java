package tech.wedev.wecom.standard.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import tech.wedev.wecom.constants.ParamsConstant;
import tech.wedev.wecom.constants.WecomApiUrlConstant;
import tech.wedev.wecom.context.TokenContextHolder;
import tech.wedev.wecom.entity.qo.ClientShareUploadQO;
import tech.wedev.wecom.entity.vo.ResponseVO;
import tech.wedev.wecom.enums.JwtKeyEnum;
import tech.wedev.wecom.standard.ZhWecomMarketArticleService;
import tech.wedev.wecom.entity.po.ZhWecomMarketArticlePO;
import tech.wedev.wecom.entity.qo.ZhWecomMarketArticleQO;
import tech.wedev.wecom.dao.BasicMapper;
import tech.wedev.wecom.dao.ZhWecomMarketArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.wedev.wecom.third.WecomRequestService;
import tech.wedev.wecom.tools.HttpProxyTools;
import tech.wedev.wecom.utils.DateUtils;
import tech.wedev.wecom.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
public class ZhWecomMarketArticleServiceImpl extends BasicServiceImpl<ZhWecomMarketArticlePO, ZhWecomMarketArticleQO> implements ZhWecomMarketArticleService {

    @Autowired
    private ZhWecomMarketArticleMapper zhWecomMarketArticleMapper;
    @Autowired
    private WecomRequestService wecomRequestService;

    //OAAS服务器地址
    //    @Value ("S{oaas.Address}")
    private String oaasAddress;

    //OAAS的domain内容
    //    @Value ("S{oaas.Content}")
    private String oaasContent;

    @Override
    public BasicMapper<ZhWecomMarketArticlePO, ZhWecomMarketArticleQO> getBasicMapper() {
        return zhWecomMarketArticleMapper;
    }

    @Override
    public ResponseVO<?> uploadQiweArticle(ClientShareUploadQO clientShareUploadQO) {
        try {
            String corpId = Optional.ofNullable(TokenContextHolder.get())
                    .map(a -> a.getKey(JwtKeyEnum.CORP_ID.getCode()))
                    .map(String::valueOf)
                    .orElse(clientShareUploadQO.getCorpId());
            String contentId = clientShareUploadQO.getContentId();
            String articleTitle = clientShareUploadQO.getArticleTitle();
            String suffix = clientShareUploadQO.getSuffix();
            if (StringUtils.isBlank(contentId) || StringUtils.isBlank(articleTitle) || StringUtils.isBlank(suffix)) {
                return ResponseVO.error("上传文件参数为空！");
            }
            long MB = 1024 * 1024;
            long sizeLimit;
            String fileType;
            if ("jpg".equalsIgnoreCase(suffix) || "png".equalsIgnoreCase(suffix)) {
                sizeLimit = 10 * MB;
                fileType = "image";
            } else if ("mp4".equalsIgnoreCase(suffix)) {
                sizeLimit = 10 * MB;
                fileType = "video";
            } else if ("amr".equalsIgnoreCase(suffix)) {
                sizeLimit = 2 * MB;
                fileType = "voice";
            } else {
                sizeLimit = 20 * MB;
                fileType = "file";
            }
            //拉取OAAS文件
            String url = "http://" + oaasAddress + "/api/v1/chunkserver/" + oaasContent + "/_/" + contentId;
            log.info("ZhWecomMarketArticleServiceImpl.uploadQiweArticle url:" + url);
            byte[] fileStream = HttpProxyTools.getFileByHttpUrl(url);
            //校验文件大小
            long fileSize = fileStream.length;
            if ((double) fileSize / sizeLimit >= 1.0) {
                return ResponseVO.error("文件大小超出企微平台限制！");
            }
            String fileName = articleTitle + "." + suffix;
            Map<String, Object> body = new HashMap<>();
            body.put("fileName", fileName);
            body.put("fileSuffix", suffix);
            body.put("fileType", fileType);
            body.put("fileLength", fileSize);
            body.put("fileStream", fileStream);
            //调用企微通用接口
            String qiWeUrl = WecomApiUrlConstant.MEDIA_UPLOAD.replace("%2$s", fileType);
            JSONObject resultObject = (JSONObject) wecomRequestService.generalCallQiWeApi(corpId, ParamsConstant.METHOD_POST, body, qiWeUrl);
            log.info("##调用企微上传临时素材接口上送参数 corpId: " + corpId + "body: " + JSONObject.toJSONString(body) + "url: " + qiWeUrl);
            log.info("##调用企微上传临时素材接口返回结果: " + JSONObject.toJSONString(resultObject));
            if (!"0".equals(String.valueOf(resultObject.get("errcode")))) {
                log.error("##调用企微上传临时素材接口异常: " + resultObject.get("errmsg"));
                ResponseVO.error(resultObject.get("errmsg"));
            }
            String mediaId = (String) resultObject.get("media_id");
            String createdAt = resultObject.get("created_at") + "000";

            Long a = Long.valueOf(createdAt);
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a);
            Date date = DateUtils.formatStrToDate(currentTime);
            int update = zhWecomMarketArticleMapper.updateByArticleSourceAndArticleApp(clientShareUploadQO, mediaId, date);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("mediaId", mediaId);
            resultMap.put("createdAt", date);
            return ResponseVO.success(resultMap);
        } catch (Exception e) {
            log.error("##触客端去分享上传临时素材异常" + JSONObject.toJSONString(clientShareUploadQO), e);
            return ResponseVO.error("触客端去分享上传临时素材异常!", null);
        }
    }
}