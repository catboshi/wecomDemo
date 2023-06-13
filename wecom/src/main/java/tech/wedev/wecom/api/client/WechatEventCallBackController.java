package tech.wedev.wecom.api.client;

import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import tech.wedev.wecom.api.entity.BaseFunctional;
import tech.wedev.wecom.constants.WecomApiUrlConstant;
import tech.wedev.wecom.entity.po.ZhWecomMarketArticlePO;
import tech.wedev.wecom.entity.po.ZhWelcomeMessageCfgPO;
import tech.wedev.wecom.entity.qo.ClientShareUploadQO;
import tech.wedev.wecom.enums.*;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.wedev.wecom.api.entity.WechatEventCallBack;
import tech.wedev.wecom.api.utils.WXBizMsgCrypt;
import tech.wedev.wecom.dao.ZhCustMgrMapMapper;
import tech.wedev.wecom.dao.ZhOrgMapper;
import tech.wedev.wecom.dao.ZhWecomMarketArticleMapper;
import tech.wedev.wecom.dao.ZhWelcomeMessageCfgMapper;
import tech.wedev.wecom.entity.po.ZhCustMgrMapPO;
import tech.wedev.wecom.entity.po.ZhOrgPO;
import tech.wedev.wecom.entity.qo.GenParamBasicQO;
import tech.wedev.wecom.entity.qo.ZhQywxContactConfigInfoQO;
import tech.wedev.wecom.exception.BusinessException;
import tech.wedev.wecom.exception.ExceptionAssert;
import tech.wedev.wecom.exception.ExceptionCode;
import tech.wedev.wecom.exception.WecomException;
import tech.wedev.wecom.personalized.impl.AsynTaskDtlServiceImpl;
import tech.wedev.wecom.request.RequestV1Private;
import tech.wedev.wecom.standard.GenParamBasicService;
import tech.wedev.wecom.standard.ZhQywxContactConfigInfoService;
import tech.wedev.wecom.standard.ZhWecomMarketArticleService;
import tech.wedev.wecom.third.WecomRequestService;
import tech.wedev.wecom.utils.DateUtils;
import tech.wedev.wecom.utils.LongUtil;
import tech.wedev.wecom.utils.ObjectUtils;
import tech.wedev.wecom.utils.StringUtils;

import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/client/cust")
public class WechatEventCallBackController {
    private static final String HEADQUARTER_CODE = "0010100000";

    //调用图文封面url前缀
//    @Value("${wecom.smsb.picurl.prefix}")
    private String wecomSmsbPicurlPrefix;
    //调用eiop缩略图Url前缀
//    @Value ("${wecom.eiop.picurl.prefix}")
    private String wecomEiopPicUrlPrefix;
    //OAAS的domain内容
//    @Value ("S{oaas.Content}")
    private String oaasContent;

    @Autowired
    private GenParamBasicService genParamBasicService;
    @Autowired
    private AsynTaskDtlServiceImpl asynTaskDtlService;
    @Autowired
    private ZhQywxContactConfigInfoService contactConfigInfoService;
    @Autowired
    private WecomRequestService wecomRequestService;
    @Autowired
    private ZhWelcomeMessageCfgMapper welcomeMessageCfgMapper;
    @Autowired
    private ZhWecomMarketArticleMapper wecomMarketArticleMapper;
    @Autowired
    private ZhOrgMapper orgMapper;
    @Autowired
    private ZhCustMgrMapMapper custMgrMapMapper;
    @Autowired
    private ZhWecomMarketArticleService wecomMarketArticleService;

    @GetMapping("/eventcallback/V1")
    public String eventCallBackGet(@RequestParam("gywxcorpid") String qywxCorpId,
                                   @RequestParam("msg_signature") String msgSignature,
                                   @RequestParam("timestamp") String timeStamp,
                                   @RequestParam("nonce") String nonce,
                                   @RequestParam("echostr") String echoStr) {

        log.info("好友事件回调EventControlLer收到验证清求：" + timeStamp);
        String result = null;
        try {
            //参数urldecode
            msgSignature = URLDecoder.decode(msgSignature, "UTF-8");
            timeStamp = URLDecoder.decode(timeStamp, "UTF-8");
            nonce = URLDecoder.decode(nonce, "UTF-8");
            echoStr = URLDecoder.decode(echoStr, "UTF-8");
            log.info("msgSignature:" + msgSignature);
            log.info("timestamp:" + timeStamp);
            log.info("nonce:" + nonce);
            echoStr = echoStr.replaceAll(" ", "+");
            log.info("echoStr:" + echoStr);
            qywxCorpId = URLDecoder.decode(qywxCorpId, "UTF-8");
            log.info("gywxCorpId:" + qywxCorpId);

            // 获取Token、EncodingAESKey
            String token = genParamBasicService.queryWecomGenParam(GenParamBasicQO.builder().paramType(GenParamBasicParamTypeEnum.WECOM_OUTER_NOTIFY).paramCode(GenParamBasicParamCodeEnum.OUTER_TOKEN).build());
            String encodingAESKey = genParamBasicService.queryWecomGenParam(GenParamBasicQO.builder().paramType(GenParamBasicParamTypeEnum.WECOM_OUTER_NOTIFY).paramCode(GenParamBasicParamCodeEnum.OUTER_ENCODING_AESKEY).build());
            //验签、解密和解析回调参教
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(token, encodingAESKey, qywxCorpId);
            result = wxcpt.VerifyURL(msgSignature, timeStamp, nonce, echoStr);
        } catch (Exception e) {
            log.error("eventcallback异常:", e);
            return "xx";
        }
        return result;
    }

    @PostMapping(value = "/eventcallback/V1")
    public String eventCallBackPost(@RequestParam("gywxcorpid") String qywxCorpId,
                                    @RequestParam("msg_signature") String msgSignature,
                                    @RequestParam("timestamp") String timeStamp,
                                    @RequestParam("nonce") String nonce,
                                    @RequestBody WechatEventCallBack postData) {

        log.info("好友事件回调EventController收到请求:" + timeStamp);
        String result = "";
        WXBizMsgCrypt wxcpt = null;
        try {
            //参数urldecode
            msgSignature = URLDecoder.decode(msgSignature, "UTF-8");
            timeStamp = URLDecoder.decode(timeStamp, "UTF-8");
            nonce = URLDecoder.decode(nonce, "UTF-8");
            log.info("msgSignature:" + msgSignature);
            log.info("timeStamp:" + timeStamp);
            log.info("nonce:" + nonce);

            //获取post xml信息
            String corpID = postData.getToUserName();
            log.info("postData ToUserName:" + corpID);
            String encryptMsg = postData.getEncrypt();
            log.info("postData encrypt decode:" + encryptMsg);

            String token = genParamBasicService.queryWecomGenParam(GenParamBasicQO.builder().paramType(GenParamBasicParamTypeEnum.WECOM_OUTER_NOTIFY).paramCode(GenParamBasicParamCodeEnum.OUTER_TOKEN).build());
            String encodingAESKey = genParamBasicService.queryWecomGenParam(GenParamBasicQO.builder().paramType(GenParamBasicParamTypeEnum.WECOM_OUTER_NOTIFY).paramCode(GenParamBasicParamCodeEnum.OUTER_ENCODING_AESKEY).build());
            wxcpt = new WXBizMsgCrypt(token, encodingAESKey, qywxCorpId);

            //解密后的明文串信息
            String decryptMsg = wxcpt.DecryptMsg(msgSignature, timeStamp, nonce, encryptMsg);
            log.info("decryptMsg:" + decryptMsg);
            Map<String, Object> map = XmlUtil.xmlToMap(decryptMsg);
            String wechatCallBackDataMap = JSONObject.toJSONString(map);
            log.info("wechatCallBackDataMap:" + wechatCallBackDataMap);
            String changeType = String.valueOf(map.get("ChangeType"));
            String externalUserID = String.valueOf(map.get("ExternalUserID"));
            String userID = String.valueOf(map.get("UserID"));

            //事件回调异步任务
            eventCallBackAsyncTask(wechatCallBackDataMap, changeType, externalUserID, userID);

            //发送新客户欢迎语
            pushWelcomeMessage(changeType, corpID, map, externalUserID, userID);

        } catch (WecomException we) {
            log.error("发送新客户欢迎语失败！", we);
        } catch (Exception ex) {
            log.error("eventcallback post请求异常:", ex);
            result = "回调异常:" + ex;
        }
        log.info("好友事件回调EventController响应报文:" + result);
        return StringUtils.isEmpty(result) ? "成功" : result;
    }

    private void eventCallBackAsyncTask(String wechatCallBackDataMap, String changeType, String externalUserID, String userID) {
        String taskType = "";
        //添加、编辑、删除、免验证添加好友事件回调异步任务
        //其他事件回调如：del_follow_user、delete_user不执行事件回调异步任务
        //后续追加其他事件回调请前往WechatEventCallBackChangeTypeEnum枚举类中维护
        taskType = Arrays.stream(WechatEventCallBackChangeTypeEnum.values())
                .filter(a -> a.name().equals(changeType))
                .peek(a -> log.info("事件回调ChangeType: {}", changeType))
                .map(WechatEventCallBackChangeTypeEnum::getDesc)
                .peek(a -> log.info("回调异步任务TaskType: {}", a))
                .findFirst()
                .orElse("");

        if (StringUtils.isNotBlank(taskType)) {
            //回调事件异步任务
//            AsynTaskBean taskBean = new AsynTaskBean();
//            taskBean.setTaskId(SnowFlakeUtil.getNextLongId());
//            taskBean.setKeywords(taskType);
//            taskBean.setTaskType(taskType);
//            taskBean.setSubTaskType(taskType);
//            taskBean.setState("0");
//            taskBean.setPriority(AsynTaskEnum.TaskPriorityType.NORMAL);
//            taskBean.setDealNum(new BigDecimal(0));
//            taskBean.setTimeoutLimit(BigDecimal.ZERO);
//            taskBean.setRefCol1(userID);//userid --> 客户经理id
//            taskBean.setRefCol2(externalUserID);//externalUserID --> 客户id
//            taskBean.setRefCol3(wechatCallBackDataMap);
//            asynTaskDtlService.insertAsynTask(taskBean);
//            log.info(changeType + "事件回调插入异步任务成功");
        }
    }

    private void pushWelcomeMessage(String changeType, String corpID, Map<String, Object> map, String externalUserID, String userID) {
        try {
            ExceptionAssert.isTrue(Arrays.stream(WechatEventCallBackChangeTypeEnum.values()).noneMatch(e -> e.name().equals(changeType)), "非添加外部联系人事件");
            var welcomeCode = Optional.ofNullable(map.get("WelcomeCode")).map(String::valueOf).orElseThrow(() -> new WecomException(ExceptionCode.LACK_WELCOMECODE));
            var state = Optional.ofNullable(map.get("State")).map(String::valueOf).orElseThrow(() -> new BusinessException("缺少联系我state参数"));
            contactConfigInfoService.select(ZhQywxContactConfigInfoQO.builder()
                    //state唯一,peek里的函数只执行一次或不执行
                    .state(state).build()).stream().filter(Objects::nonNull).peek(a -> {
                ExceptionAssert.ifTrue(!Objects.equals(a.getQrType().getCode(), QrTypeEnum.REMARK_CODE.getCode()), "非备注码添加");
                ExceptionAssert.ifTrue(StringUtils.isBlank(a.getWelcomeId()), "缺少welcome_id参数");
                //获取备注码欢迎语
                var cfg = welcomeMessageCfgMapper.selectByPrimaryKey(Long.valueOf(a.getWelcomeId()));
                Optional.ofNullable(cfg).orElseThrow(() -> new BusinessException("欢迎语配置信息不存在或被删除"));
                sendWelcomeMessage(corpID, externalUserID, welcomeCode, cfg);
            }).findAny().orElseThrow(() -> new BusinessException("二维码配置信息不存在或被删除"));
        } catch (BusinessException ex) {
            //获取默认欢迎语
            log.error("发送默认欢迎语", ex);
            var parentCode = orgMapper.selectParentNodeInfoByCode(Optional.ofNullable(custMgrMapMapper.selectByQywxMgrIdAndQywxCorpId(userID, corpID))
                            .map(ZhCustMgrMapPO::getOrgCode)
                            .orElseThrow(() -> new WecomException(ExceptionCode.CUSTMGR_ACCOUNT_ERROR)))
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(a -> Objects.equals(a.getParentCode(), HEADQUARTER_CODE))
                    .map(ZhOrgPO::getCode)
                    .peek(a -> log.info("一级分行机构号: ", a))
                    .findAny()
                    .orElseGet(() -> {
                        log.info("找不到一级分行机构号，更换总行机构号");
                        return HEADQUARTER_CODE;
                    });
            var defaultCfg = Optional.ofNullable(welcomeMessageCfgMapper.selectDefaultWelcomMessage("", corpID, parentCode))
                    .orElseGet(() -> {
                        log.info("分行默认欢迎语不存在或未配置，发送总行默认欢迎语");
                        return welcomeMessageCfgMapper.selectDefaultWelcomMessage("", corpID, HEADQUARTER_CODE);
                    });
            sendWelcomeMessage(corpID, externalUserID, String.valueOf(map.get("WelcomeCode")), defaultCfg);
        }
    }

    private void sendWelcomeMessage(String corpID, String externalUserID, String welcomeCode, ZhWelcomeMessageCfgPO cfgPO) {
        Optional.ofNullable(cfgPO).orElseThrow(() -> new WecomException(ExceptionCode.NOT_EXIST_DEFAULT_WECOMEMESSAGE));
        ExceptionAssert.isTrue(LongUtil.isEmpty(cfgPO.getArticleId()) &&
                StringUtils.isBlank(cfgPO.getWelcomeWord()), "欢迎语文字和资讯不能同时为空");
        ExceptionAssert.isTrue(LongUtil.isEmpty(cfgPO.getArticleId()) &&
                !Objects.equals(cfgPO.getType(), ZhWelcomeMessageCfgEnum.TypeENUM.TEXT.getCode()), "缺少article_id参数");
        var article = wecomMarketArticleMapper.checkIsValid(cfgPO.getArticleId());

        //非仅文本模式校验
        if (!Objects.equals(cfgPO.getType(), ZhWelcomeMessageCfgEnum.TypeENUM.TEXT.getCode())) {
            ExceptionAssert.isTrue(Objects.isNull(article), "资讯已失效或被删除");
        }

        //仅文本模式，不支持同时发送附件
        article = Objects.equals(cfgPO.getType(), ZhWelcomeMessageCfgEnum.TypeENUM.TEXT.getCode()) ? null : article;

        ExceptionAssert.isTrue(Objects.isNull(article) &&
                StringUtils.isBlank(cfgPO.getWelcomeWord()), "缺少欢迎语文字");
        try {
            wecomRequestService.generalCallQiWeApi(corpID, "POST", JSONObject.parseObject(JSON.toJSONString(assembleJsonField(welcomeCode, cfgPO, article, corpID)), HashMap.class), WecomApiUrlConstant.SEND_WELCOME_MSG);
            log.info("发送新客户欢迎语成功！{} ", externalUserID);

        } catch (WecomException wecomException) {
            log.info("send_welcom_message response message: {} ", wecomException.getCode() + wecomException.getMsg());
        } catch (Exception e) {
            log.error("请求API服务异常: ", "", e);
        }
    }

    private Map<String, Object> assembleJsonField(String welcomeCode, ZhWelcomeMessageCfgPO cfgPO, ZhWecomMarketArticlePO article, String corpID) {
        //region 组装报文发送
        Map<String, Object> requestMap = new HashMap<>();
        //后续发送其他附件类型请前往AttachmentsMsgTypeEnum枚举类中维护
        var msgType = Arrays.stream(AttachmentsMsgTypeEnum.values())
                .filter(a -> a.getCode().equals(cfgPO.getType()))
                .map(AttachmentsMsgTypeEnum::getDesc)
                .peek(a -> log.info("sending... the attachments.msgtype is {} ", a))
                .findFirst()
                .orElseThrow(() -> new WecomException(ExceptionCode.UNSUPPORT_MSGTYPE));

        requestMap.put("attachments", Objects.equals("text", msgType) ? null : Stream.of(getAttachments(article, msgType, corpID)).collect(Collectors.toList()));

        requestMap.put("text", RequestV1Private.Text.builder()
                .content(cfgPO.getWelcomeWord())
                .build());

        requestMap.put("welcome_code", welcomeCode);
        return requestMap;
        //endregion
    }

    private RequestV1Private.Attachments getAttachments(ZhWecomMarketArticlePO article, String msgType, String corpID) {
        return Optional.ofNullable(article).map(a -> {
            ExceptionAssert.isTrue(!(Objects.equals(a.getSourceFormat(), ZhWelcomeMessageCfgEnum.TypeENUM.H5.getCode())
                    || Objects.equals(a.getSourceFormat(), ZhWelcomeMessageCfgEnum.TypeENUM.IMAGE_TEXT.getCode())
                    || Objects.equals(a.getSourceFormat(), ZhWelcomeMessageCfgEnum.TypeENUM.APP.getCode())), "暂不支持H5、图文、小程序以外其他附件类型");

            //F-EIOP -> ~/wecom/eiop F-SMSB -> ~/wecom/smsb
            var picUrl = Objects.equals("F_EIOP", a.getArticleApp()) ?
                    (wecomEiopPicUrlPrefix + "/" + oaasContent + "/_/" + a.getThumbnail()) :
                    a.getArticleThumbnail().indexOf("/") == 0 ?
                            wecomSmsbPicurlPrefix + a.getArticleThumbnail() :
                            wecomSmsbPicurlPrefix + "/" + a.getArticleThumbnail();

            //仅判断小程序mediaId是否过期
            var mediaId = Objects.equals(a.getSourceFormat(), ZhWelcomeMessageCfgEnum.TypeENUM.APP.getCode()) ? (Objects.isNull(a.getMediaCreatedTime()) ||
                    ObjectUtils.strToType(DateUtils.printDays(a.getMediaCreatedTime(), DateUtils.currentDate()), Integer.class) >= 3 ? null : a.getMediaId()) : "";

            var pic_media_id = Optional.ofNullable(mediaId).orElseGet(() -> {
                var result = wecomMarketArticleService.uploadQiweArticle(ClientShareUploadQO.builder()
                        .articleApp(a.getArticleApp())
                        .articleSource(a.getArticleSource())
                        .articleTitle(a.getArticleTitle())
                        .contentId(a.getThumbnail())
                        .corpId(corpID)
                        .suffix(a.getThumbnailFormat())
                        .build());

                String newMediaId = "";
                if (result.getRetCode() == 200) {
                    var resultMap = ObjectUtils.strObjToType(result.getData(), HashMap.class);
                    newMediaId = MapUtils.getString(resultMap, "mediaId");
                }
                return newMediaId;
            });

            Arrays.stream(AttachmentsMsgTypeEnum.values()).forEach(val -> val.setValue(picUrl, pic_media_id));
            return (RequestV1Private.Attachments) Stream.of(new BaseFunctional<>(msgType)).map(m -> m.map(msgtype -> BaseFunctional.transform(article, msgtype))).findFirst().get().data;
        }).orElse(null);
    }
}

