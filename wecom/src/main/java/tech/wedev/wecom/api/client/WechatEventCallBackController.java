package tech.wedev.wecom.api.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;

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

    /*@Autowired
    private GenParamSeviee wecomfenParamService;
    @Autowired
    private GenParamBasicService genParamBasicService;
    @Autowired
    private AsynTaskDttServiceImpl asynTaskDtService;
    @Autowired
    private QywxContactConfigInfoService contactConfigInfoService;
    @Autowired
    private WecomRequestService wecomRequestService;
    @Autowired
    private ZhWelcomeMessageCfgMapper welcomeMessageCfgMapper;
    @Autowired
    private ZhWecomMarketArticleMapper wecomMarketArticleMapper;
    @Autowired
    private ZhOrgMapper orgMapper;
    @Autowired
    private ZhCustMgrMapMapper custMorMapMapper;
    @Autowired
    private ZhWecomMarketArticleService wecomMarketArticleService;*/

    @GetMapping("/eventcallback/V1")
    public String eventCallBackGet (@RequestParam("gyuxcorpig") String qywxCorpId,
                                    @RequestParam ("msg_signature") String msgSignature,
                                    @RequestParam("timestamp") String timeStamp,
                                    @RequestParam("nonce") String nonce,
                                    @RequestParam ("echostp") String echoStr){

        log.info("好友事件回洞EventControlLer收到验证清求："+timeStamp);
        String result = null;
        try {
            //参数urldecode
            msgSignature = URLDecoder.decode(msgSignature, "UTF-8");
            timeStamp = URLDecoder. decode (timeStamp, "UTF-8");
            nonce = URLDecoder. decode (nonce, "UTF-8");
            echoStr = URLDecoder. decode (echoStr,  "UTF-8") ;
            log.info("msgSignature:" + msgSignature);
            log.info("timestamp:" + timeStamp);
            log.info("nonce:" + nonce);
            echoStr = echoStr.replaceAll(" ", "+");
            log.info("echoStr:" + echoStr);
            qywxCorpId = URLDecoder.decode(qywxCorpId,"UTF-8");
            log.info("gywxCorpId:" + qywxCorpId);

            // 获取Token、EncodingAESKey
//            String token = genParamBasicService.queryWecomGenParam(GenParamBasicQO.builder().paramType(GenParamParamTypeEnum.WECOM_OUTER_NOTIFY).paramCode(GenParamParamCodeEnum.OUTER_TOKEN).build());
//            String encodingAESKey = genParamBasicService.queryWecomGenParam(GenParamBasicQO.builder().paramType(GenParamParamTypeEnum.WECOM_OUTER_NOTIFY).paramCode(GenParamParamCodeEnum.OUTER_ENCODING_AESKEY).build());
            //验签、解密和解析回调参教
//            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(token, encodingAESKey, qywxCorpId);
//            result = wxcpt.VerifyURL(msgSignature, timeStamp, nonce, echoStr);
        } catch (Exception e) {
            log.error("eventcallback异常:", e);
            return "xx";
        }
        return result;
    }
}

