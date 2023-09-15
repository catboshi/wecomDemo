package tech.wedev.wecom.standard.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;
import tech.wedev.wecom.mapper.OpLogMapper;
import tech.wedev.wecom.mapper.WecomMarketArticleMapper;
import tech.wedev.wecom.entity.po.OpLogPO;
import tech.wedev.wecom.entity.po.WecomMarketArticlePO;
import tech.wedev.wecom.entity.vo.ResponseVO;
import tech.wedev.wecom.enums.OpTypeEnum;
import tech.wedev.wecom.standard.ClientMsgReadLogService;
import tech.wedev.wecom.third.WecomRequestService;
import tech.wedev.wecom.utils.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 资讯阅读日志记录服务实现
 */
@Service
public class ClientMsgReadLogServiceImpl implements ClientMsgReadLogService {

    @Resource
    private WecomRequestService wecomRequestService;

    @Resource
    private OpLogMapper opLogMapper;

    @Resource
    WecomMarketArticleMapper wecomMarketArticleMapper;

    @Override
    public ResponseVO saveLog(String articleSource, String code) {
        ResponseVO<Object> responseVO = ResponseVO.error();
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(articleSource)) {
            responseVO.setRetMsg("参数为空");
            return responseVO;
        }
        //根据articleSource查询status=1的资讯
        WecomMarketArticlePO wecomMarketArticlePO = wecomMarketArticleMapper.selectOneByArticleSource(articleSource);
        if (wecomMarketArticlePO == null) {
            responseVO.setRetMsg("该资讯不存在");
            return responseVO;
        }

        if (wecomMarketArticlePO.getIsDeleted() == 1) {
            responseVO.setRetMsg("该资讯已被发布者删除");
            return responseVO;
        }
        if (wecomMarketArticlePO.getStatus() == 3) {
            responseVO.setRetMsg("该资讯已作废");
            return responseVO;
        }
        if (StringUtils.isEmpty(wecomMarketArticlePO.getArticleLink())) {
            responseVO.setRetMsg("跳转失败，链接为空，请检查！");
            return responseVO;
        }
        Map<String, Object> authResult = wecomRequestService.authGetUserInfo(code);
        if ((Integer) authResult.get("errcode") != 0) {
            responseVO.setRetMsg("调用企微API返回错误");
            return responseVO;
        }
        String userId = MapUtils.getString(authResult, "userid");
        Map<String, String> content = new HashMap<>();
        content.put("article_source", articleSource);
        JSONObject log = new JSONObject();
        log.put("data", content);
        OpLogPO opLogPO = OpLogPO.builder()
                .opUserId(userId)
                .opTellerno("测试统一认证号")
                .isDeleted(0)
                .createId(0L)
                .gmtCreate(new Date())
                .gmtModified(new Date())
                .modifiedId(0L)
                .opType(OpTypeEnum.READ_MSG.getCode())
                .corpId("测试租户")
                .orgCode("测试机构")
                .opContent(JSON.toJSONString(log)).build();
        opLogMapper.saveLog(opLogPO);
        responseVO = ResponseVO.success();
        responseVO.setRetMsg(wecomMarketArticlePO.getArticleLink());
        return responseVO;
    }
}
