package tech.wedev.wecom.third.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tech.wedev.wecom.constants.ParamsConstant;
import tech.wedev.wecom.constants.WecomApiUrlConstant;
import tech.wedev.wecom.context.TokenContextHolder;
import tech.wedev.wecom.dao.ZhCorpInfoMapper;
import tech.wedev.wecom.entity.bo.AccessCredentialsCommand;
import tech.wedev.wecom.entity.po.GenParamBasicPO;
import tech.wedev.wecom.entity.po.GenParamPO;
import tech.wedev.wecom.entity.po.ZhCorpInfo;
import tech.wedev.wecom.entity.qo.GenParamBasicQO;
import tech.wedev.wecom.entity.qo.GenParamQO;
import tech.wedev.wecom.enums.*;
import tech.wedev.wecom.exception.ExceptionAssert;
import tech.wedev.wecom.exception.ExceptionCode;
import tech.wedev.wecom.exception.WecomException;
import tech.wedev.wecom.standard.GenParamBasicService;
import tech.wedev.wecom.standard.GenParamService;
import tech.wedev.wecom.third.WecomRequestService;
import tech.wedev.wecom.utils.*;

import java.util.*;

@Slf4j
@Service
@PropertySource(value = "classpath:META-INF/global-config.properties")
public class WecomRequestServiceImpl implements WecomRequestService {

    @Autowired
    private GenParamService genParamService;
    @Autowired
    private GenParamBasicService genParamBasicService;

    @Autowired
    private ZhCorpInfoMapper zhCorpInfoMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Value("${wecom.api.url.prefix}")
    private String wecomApiUrlPrefix;

    @Override
    public Object generalCallQiWeApi(String corpId, String methodType, Map<String, Object> body, String urlConstant) {
        JSONObject resultObject = new JSONObject();
        //获取请求的token
        AccessCredentialsCommand accessTokenCommand = AccessCredentialsCommand.builder().corpId(corpId)
                .interfaceIdentifyUrl(getContextConstant(urlConstant)).build();
        String token = this.generateAccessToken(accessTokenCommand);

        if (token == null) {
            throw new WecomException(400, "获取token失败，corpInfo配置信息不存在");
        }

        //处理接口请求参数
        String url = getRequestQiWeUrl(methodType, body, urlConstant, token);
        log.info("WecomRequestServiceImpl###generalCallQiWeApi###企微API URL:" + url);

        try {
            //根据请求类型发起请求
            resultObject = doHttpByMethodType(methodType, body, url);
        } catch (Exception e) {
            log.error("WecomRequestServiceImpl###generalCallQiWeApi###企微API调用异常", e);
            throw new WecomException(ExceptionCode.REQUEST_ERROR);
        }

        int errCode = resultObject.getIntValue("errcode");
        if (errCode == 0) {
            //根据不同的请求接口，返回所需的结果
            return createReturnInfo(resultObject, urlConstant);
        } else if (errCode == 42001) {
            //TOKEN失败，获取TOKEN后重新调用
            this.refreshTokenFromZhCorpInfo(corpId, getContextConstant(urlConstant));
            return this.generalCallQiWeApi(corpId, methodType, body, urlConstant);
        } else {
            throw new WecomException(errCode, "调用企业微信API返回错误");
        }
    }

    @Override
    public String generateAccessToken(AccessCredentialsCommand command) {
        log.info("WecomRequestServiceImpl.generateAccessToken###入参：" + JSONObject.toJSONString(command));
        String token = "";
        String corpId = command.getCorpId();
        String interfaceIdentifyUrl = command.getInterfaceIdentifyUrl();


        //从redis缓存读取token
        String tokenKey = Joiner.on("_").join(corpId, ParamsConstant.TOKEN_NOS_KEY, command.getInterfaceIdentifyUrl());

        token = (String) redisUtils.get(tokenKey);
        if (StringUtils.isNotBlank(token)) {
            //缓存中存在token，直接返回
            log.info("WecomRequestServiceImpl###generateAccessToken###redis缓存token: " + token);
            return token;
        }


        //redis缓存token不存在或redis异常，从数据库读取token
        GenParamEnum paramType = this.paramTypeMatch(interfaceIdentifyUrl);
        log.info("WecomRequestServiceImpl###generateAccessToken###匹配类型: " + paramType);
        List<ZhCorpInfo> tokenInDB = zhCorpInfoMapper.findByCorpId(corpId);
        //数据库存在token且token有效，直接返回
        if (tokenInDB == null || tokenInDB.isEmpty()) {
            log.error("WecomRequestServiceImpl###generateAccessToken###zhCorpInfo异常，无该corpId相关配置");
            return null;
        }

        Integer tokenTimeOut = ParamsConstant.TOKEN_TIME_OUT;
        if (!CollectionUtils.isEmpty(tokenInDB)) {
            try {
                Date tokenModified = findTokenModified(paramType, tokenInDB.get(0));
                if (!Objects.isNull(tokenModified)) {
                    tokenTimeOut = Integer.valueOf(DateUtils.printDiffer(tokenModified, DateTime.now()));
                }
            } catch (Exception e) {
                log.error("WecomRequestServiceImpl###generateAccessToken###parse tokenTimeOut异常: ", "", e);
            }
        }

        if (!CollectionUtils.isEmpty(tokenInDB) && tokenTimeOut < ParamsConstant.TOKEN_TIME_OUT
                && StringUtils.isNotEmpty(findTokenFromZhCorpInfoDB(paramType, tokenInDB.get(0)))) {
            log.info("WecomRequestServiceImpl###generateAccessToken###数据库缓存token:" + JSONObject.toJSONString(tokenInDB.get(0)));
            return findTokenFromZhCorpInfoDB(paramType, tokenInDB.get(0));
        }

        //数据库token不存在或失效，重新获取token
        token = this.getTokenFromQiweApi(paramType, tokenInDB.get(0));
        log.info("WecomRequestServiceImpl###generateAccessToken###新token: " + token);

        //更新数据库token，设置redis缓存
        updateDbTokenOrTicket(paramType, ParamsConstant.TYPE_TOKEN, token, corpId, interfaceIdentifyUrl);
        return token;
    }

    @Override
    public Map<String, Object> externalContactGet(String corpId, String externalUserId) {
        log.info("WecomRequestServiceImpl.externalContactGet###企微API入参: \"corpId\": " + corpId + ", \"externalUserId\": " + externalUserId);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("external_userid", externalUserId);
        return ((JSONObject) generalCallQiWeApi(corpId, ParamsConstant.METHOD_GET, requestBody, WecomApiUrlConstant.EXTERNAL_CONTACT_GET)).getInnerMap();
    }

    private String getRedisErrorCode(GenParamEnum paramType) {
        String errCode = "";
        if (AccessCredentialsEnum.UrlParamType.APPLICATION.getCode().equals(paramType.getName())) {
            errCode = APIErrorMsgEnum.REDIS.APPLICATION.getCode();
        } else if (AccessCredentialsEnum.UrlParamType.MSG_AUDIT.getCode().equals(paramType.getName())) {
            errCode = APIErrorMsgEnum.REDIS.MSG_AUDIT.getCode();
        } else if (AccessCredentialsEnum.UrlParamType.COMMUNICATION.getCode().equals(paramType.getName())) {
            errCode = APIErrorMsgEnum.REDIS.COMMUNICATION.getCode();
        } else if (AccessCredentialsEnum.UrlParamType.EXTERNAL_CONTACT.getCode().equals(paramType.getName())) {
            errCode = APIErrorMsgEnum.REDIS.EXTERNAL_CONTACT.getCode();
        }
        return errCode;
    }

    /**
     * token/secret类型匹配
     *
     * @param context
     * @return
     */
    private GenParamEnum paramTypeMatch(String context) {
        GenParamEnum type = null;
        if (this.contextMatch(context, GenParamEnum.COMMUNICATION)) {
            type = GenParamEnum.COMMUNICATION;
        } else if (this.contextMatch(context, GenParamEnum.EXTERNAL_CONTACT)) {
            type = GenParamEnum.EXTERNAL_CONTACT;
        } else if (this.contextMatch(context, GenParamEnum.MSG_AUDIT)) {
            type = GenParamEnum.MSG_AUDIT;
        } else if (this.contextMatch(context, GenParamEnum.APPLICATION)) {
            type = GenParamEnum.APPLICATION;
        }
        return type;
    }

    /**
     * @param context 请求url
     * @param type    判断调用企业微信不同token的类型
     * @return
     */
    private boolean contextMatch(String context, GenParamEnum type) {
        String[] urlSegment;
        switch (type) {
            case EXTERNAL_CONTACT:
                urlSegment = genParamBasicService.queryWecomGenParamValue(GenParamBasicParamTypeEnum.WECOM_API, GenParamBasicParamCodeEnum.EXTERNAL_CONTACT).split(",");
                break;
            case MSG_AUDIT:
                urlSegment = genParamBasicService.queryWecomGenParamValue(GenParamBasicParamTypeEnum.WECOM_API, GenParamBasicParamCodeEnum.MSG_AUDIT).split(",");
                break;
            case APPLICATION:
                urlSegment = genParamBasicService.queryWecomGenParamValue(GenParamBasicParamTypeEnum.WECOM_API, GenParamBasicParamCodeEnum.APPLICATION).split(",");
                break;
            default:
                //默认COMMUNICATION类型
                urlSegment = genParamBasicService.queryWecomGenParamValue(GenParamBasicParamTypeEnum.WECOM_API, GenParamBasicParamCodeEnum.COMMUNICATION).split(",");
        }
        context = context.replace("/cgi-bin", "");
        boolean res = false;
        for (int i = 0; i < urlSegment.length; i++) {
            res = res || context.equals(urlSegment[i]);
            if (res) {
                break;
            }
        }
        return res;
    }

    /**
     * 根据paramType匹配ZhCorpInfo对应Token值
     *
     * @param paramType
     * @param zhCorpInfo
     * @return
     */
    private String findTokenFromZhCorpInfoDB(GenParamEnum paramType, ZhCorpInfo zhCorpInfo) {
        if (AccessCredentialsEnum.UrlParamType.APPLICATION.getCode().equals(paramType.getName())) {
            return zhCorpInfo.getTokenApplication();
        } else if (AccessCredentialsEnum.UrlParamType.MSG_AUDIT.getCode().equals(paramType.getName())) {
            return zhCorpInfo.getTokenMsgAudit();
        } else if (AccessCredentialsEnum.UrlParamType.COMMUNICATION.getCode().equals(paramType.getName())) {
            return zhCorpInfo.getTokenCommunication();
        } else if (AccessCredentialsEnum.UrlParamType.EXTERNAL_CONTACT.getCode().equals(paramType.getName())) {
            return zhCorpInfo.getTokenExternalContact();
        }
        return "";
    }

    /**
     * 根据paramType匹配ZhCorpInfo对应Secret值
     * @param paramType
     * @param zhCorpInfo
     * @return
     */
    private String findSecretFromZhCorpInfoDB(GenParamEnum paramType, ZhCorpInfo zhCorpInfo) {
        if (AccessCredentialsEnum.UrlParamType.APPLICATION.getCode().equals(paramType.getName())) {
            return zhCorpInfo.getSecretApplication();
        } else if (AccessCredentialsEnum.UrlParamType.MSG_AUDIT.getCode().equals(paramType.getName())) {
            return zhCorpInfo.getSecretMsgAudit();
        } else if (AccessCredentialsEnum.UrlParamType.COMMUNICATION.getCode().equals(paramType.getName())) {
            return zhCorpInfo.getSecretCommunication();
        } else if (AccessCredentialsEnum.UrlParamType.EXTERNAL_CONTACT.getCode().equals(paramType.getName())) {
            return zhCorpInfo.getSecretExternalContact();
        }
        return "";
    }


    /**
     * 从企业微信获取token
     *
     * @param paramType
     * @return
     */
    private String getTokenFromWecom(GenParamEnum paramType) {
        //1. 查询secret参数
        List<GenParamPO> secretInDB = this.getParamFromDB(ParamsConstant.TYPE_SECRET, paramType);
        log.info("WecomRequestServiceImpl###getTokenFromWecom###SECRET: " + JSON.toJSONString(secretInDB));
        ExceptionAssert.isTrue(secretInDB.isEmpty(), ExceptionCode.PARAMETER_SECRET_ERROR);
        //2. 查询corpId参数
        List<GenParamPO> corpIdInDB = this.getParamFromDB(ParamsConstant.TYPE_CORP_ID, paramType);
        log.info("WecomRequestServiceImpl###getTokenFromWecom###CORP_ID: " + JSON.toJSONString(corpIdInDB));
        ExceptionAssert.isTrue(corpIdInDB.isEmpty(), ExceptionCode.PARAMETER_CORP_ID_ERROR);
        //调用企业微信API
        String url = wecomApiUrlPrefix + WecomApiUrlConstant.GET_TOKEN;
        String token;
        try {
            JSONObject accessToken = HttpRequestUtils.getAccessResult(String.format(url, corpIdInDB.get(0).getParamValue(), secretInDB.get(0).getParamValue()));
            int errCode = accessToken.getIntValue("errcode");
            String errMsg = accessToken.getString("errmsg");
            log.info("调用企微接口获取access_token返回结果，errcode=" + errCode + "##errmsg=" + errMsg);
            ExceptionAssert.isTrue(errCode != 0, "调用企微接口获取access_token失败，错误码: " + errCode);
            token = accessToken.getString("access_token");
        } catch (Exception e) {
            log.error("WecomRequestServiceImpl###getTokenFromQiweApi###调用企微接口获取access_token失败", e);
            throw new WecomException(ExceptionCode.REQUEST_ERROR);
        }
        return token;
    }

    /**
     * 查询数据库参数，默认查询CORP_ID
     *
     * @param type      token/secret/corpId
     * @param paramType token/secret类型
     * @return
     */
    private List<GenParamPO> getParamFromDB(String type, GenParamEnum paramType) {
        GenParamQO genParamQO = new GenParamQO();
        genParamQO.setIsDeleted(BaseDeletedEnum.EXISTS.getCode());
        genParamQO.setParamType(GenParamEnum.CORP_ID.getParamTypeEnum().getName());
        genParamQO.setParamCode(GenParamEnum.CORP_ID.getName());
        genParamQO.setCorpIds(TokenContextHolder.getCorpIds());

        if (!ParamsConstant.TYPE_CORP_ID.equals(type) && paramType != null) {
            genParamQO.setParamType(paramType.getParamTypeEnum().getName());
            genParamQO.setParamCode(type + paramType.getName());
        }
        return genParamService.select(genParamQO);
    }

    private String getTokenFromQiweApi(GenParamEnum paramType, ZhCorpInfo zhCorpInfo) {
        //查询secret参数
        String secretInDB = this.findSecretFromZhCorpInfoDB(paramType, zhCorpInfo);
        log.info("WecomRequestServiceImpl###getTokenFromQiweApi###SECRET: " + secretInDB);
        ExceptionAssert.isTrue(secretInDB.isEmpty(), ExceptionCode.PARAMETER_SECRET_ERROR);

        String corpIdInDB = zhCorpInfo.getCorpId();
        //调用企微API
        String url = wecomApiUrlPrefix + WecomApiUrlConstant.GET_TOKEN;
        String token;
        try {
            JSONObject accessToken = HttpRequestUtils.getAccessResult(String.format(url, corpIdInDB, secretInDB));
            int errCode = accessToken.getIntValue("errcode");
            String errMsg = accessToken.getString("errmsg");
            log.info("调用企微接口获取access_token返回结果，errcode=" + errCode + "##errmsg=" + errMsg);
            ExceptionAssert.isTrue(errCode != 0, "调用企微接口获取access_token失败，错误码: " + errCode);
            token = accessToken.getString("access_token");
        } catch (Exception e) {
            log.error("WecomRequestServiceImpl###getTokenFromQiweApi###调用企微接口获取access_token失败", e);
            throw new WecomException(ExceptionCode.REQUEST_ERROR);
        }
        return token;
    }

    private Date findTokenModified(GenParamEnum paramType, ZhCorpInfo zhCorpInfo) {
        if (AccessCredentialsEnum.UrlParamType.APPLICATION.getCode().equals(paramType.getName())) {
            return zhCorpInfo.getTokenApplicationModified();
        } else if (AccessCredentialsEnum.UrlParamType.MSG_AUDIT.getCode().equals(paramType.getName())) {
            return zhCorpInfo.getTokenMsgAuditModified();
        } else if (AccessCredentialsEnum.UrlParamType.COMMUNICATION.getCode().equals(paramType.getName())) {
            return zhCorpInfo.getTokenCommunicationModified();
        } else if (AccessCredentialsEnum.UrlParamType.EXTERNAL_CONTACT.getCode().equals(paramType.getName())) {
            return zhCorpInfo.getTokenExternalContactModified();
        } else if (AccessCredentialsEnum.UrlParamType.AGENT_JSAPI_TICKET.getCode().equals(paramType.getName())) {
            return zhCorpInfo.getAgentJsapiTicketModified();
        } else if (AccessCredentialsEnum.UrlParamType.CORP_JSAPI_TICKET.getCode().equals(paramType.getName())) {
            return zhCorpInfo.getCorpJsapiTicketModified();
        }
        return null;
    }

    /**
     * TOKEN失败，重新调用企微API获取TOKEN
     * 多租户
     *
     * @param corpId
     * @param interfaceIdentifyUrl
     * @return
     */
    private String refreshTokenFromZhCorpInfo(String corpId, String interfaceIdentifyUrl) {
        log.info(interfaceIdentifyUrl + "###access_token失败，重新获取");
        //根据URL获取API类型
        GenParamEnum paramType = this.paramTypeMatch(interfaceIdentifyUrl);
        //调用企微API获取TOKEN
        AccessCredentialsCommand accessTokenCommand = AccessCredentialsCommand.builder().corpId(corpId)
                .interfaceIdentifyUrl(interfaceIdentifyUrl).build();
        log.info("WecomRequestServiceImpl###refreshTokenFromZhCorpInfo###accessTokenCommand info: " + JSONObject.toJSONString(accessTokenCommand));
        String token = this.generateAccessToken(accessTokenCommand);
        List<ZhCorpInfo> tokenInDB = zhCorpInfoMapper.findByCorpId(corpId);
        ExceptionAssert.isTrue(CollectionUtils.isEmpty(tokenInDB), ExceptionCode.PARAMETER_CORP_ID_ERROR);


        //更新数据库token，设置redis缓存
        this.updateDbTokenOrTicket(paramType, ParamsConstant.TYPE_TOKEN, token, corpId, interfaceIdentifyUrl);
        return token;
    }


    /**
     * token更新后更新参数表和zhCorpInfo表token
     *
     * @param paramType
     * @param tokenTicketType
     * @param tokenOrTicket
     * @param corpId
     * @param context
     */
    private void updateDbTokenOrTicket(GenParamEnum paramType, String tokenTicketType, String tokenOrTicket, String corpId, String context) {
        //更新数据token，设置redis缓存
        List<ZhCorpInfo> tokenInDB = zhCorpInfoMapper.findByCorpId(corpId);
        this.updateTokenFromZhCorpInfo(paramType, tokenTicketType, tokenOrTicket, tokenInDB.get(0), context);

        //更新参数表token，设置redis缓存
//        List<GenParamPO> tokenInDBParam = this.getParamFromDB(tokenTicketType, paramType);
        List<GenParamBasicPO> tokenInDBParam = genParamBasicService.select(GenParamBasicQO.builder().isDeleted(BaseDeletedEnum.EXISTS).paramType(EnumUtils.getByStringCode(GenParamBasicParamTypeEnum.class, paramType.getParamTypeEnum().getName()))
                .paramCode(EnumUtils.getByStringCode(GenParamBasicParamCodeEnum.class, paramType.getName())).orderBys(ArrayUtils.asArrayList("id_0")).corpIds(ArrayUtils.asArrayNotNull(corpId, "SYSTEM")).build());

        this.updateTokenFromWecom(corpId, paramType, tokenTicketType, tokenOrTicket, tokenInDBParam, context);
    }

    /**
     * 更新数据库，redis缓存TOKEN
     * 多租户
     *
     * @param paramType
     * @param tokenTicketType
     * @param tokenOrTicket
     * @param zhCorpInfo
     * @param interfaceIdentifyUrl
     */
    private void updateTokenFromZhCorpInfo(GenParamEnum paramType, String tokenTicketType, String tokenOrTicket, ZhCorpInfo zhCorpInfo,
                                           String interfaceIdentifyUrl) {
        String corpId = zhCorpInfo.getCorpId();

        ZhCorpInfo zhCorpInfoUpd = new ZhCorpInfo();
        zhCorpInfoUpd.setId(zhCorpInfo.getId());
        if (ParamsConstant.TYPE_TICKET.equals(tokenTicketType)) {
            //更新对应ticket字段值
            setZhCorpInfoTicket(zhCorpInfoUpd, tokenOrTicket, paramType);
        } else {
            //更新对应token字段值
            setZhCorpInfoToken(zhCorpInfoUpd, tokenOrTicket, paramType);
        }
        zhCorpInfoUpd.setGmtModified(new Date());
        zhCorpInfoUpd.setCorpId(zhCorpInfo.getCorpId());
        zhCorpInfoUpd.setModifiedId(0L);
        int modify = zhCorpInfoMapper.updateByPrimaryKeySelective(zhCorpInfoUpd);

        if (modify != 0) {
            //数据库token更新成功，更新redis缓存
            //NX: 只在键不存在时，才对键进行设置操作
            //XX: 只在键存在时，才对键进行设置操作
            //EX: 设置键的过期单位为 second 秒
            try {
                if (ParamsConstant.TYPE_TICKET.equals(tokenTicketType)) {
                    redisUtils.set(corpId + ParamsConstant.NOS_KEY_SEPARATE + paramType.getName(), tokenOrTicket, ParamsConstant.TOKEN_TIME_OUT);
                } else {
                    String tokenKey = Joiner.on(ParamsConstant.NOS_KEY_SEPARATE).join(corpId, ParamsConstant.TOKEN_NOS_KEY, interfaceIdentifyUrl);
                    redisUtils.set(tokenKey, tokenOrTicket, ParamsConstant.TOKEN_TIME_OUT);
                }
            } catch (Exception e) {
                log.error("WecomRequestServiceImpl###updateTokenFromZhCorpInfo###设置redis缓存异常");
            }
        }
    }

    /**
     * 更新数据库，redis缓存TOKEN
     *
     * @param corpId
     * @param paramType
     * @param tokenTicketType
     * @param tokenOrTicket
     * @param tokenInDB
     * @param context
     */
    private void updateTokenFromWecom(String corpId, GenParamEnum paramType, String tokenTicketType, String tokenOrTicket, List<GenParamBasicPO> tokenInDB, String context) {
        GenParamPO tokenSaveToDB = new GenParamPO();
        tokenSaveToDB.setParamType(paramType.getParamTypeEnum().getName());
        if (ParamsConstant.TYPE_TICKET.equals(tokenTicketType)) {
            tokenSaveToDB.setParamCode(paramType.getName());
        } else {
            tokenSaveToDB.setParamCode(ParamsConstant.TYPE_TOKEN + paramType.getName());
        }
        tokenSaveToDB.setParamValue(tokenOrTicket);
        tokenSaveToDB.setGmtCreate(new Date());
        tokenSaveToDB.setGmtModified(new Date());
        tokenSaveToDB.setCreateId(0L);
        tokenSaveToDB.setCorpId(corpId);
        tokenSaveToDB.setModifiedId(0L);
        int modify;
        if (tokenInDB.isEmpty()) {
            modify = genParamService.save(tokenSaveToDB);
        } else {
            modify = genParamService.updateWecomGenParam(tokenSaveToDB);
        }
        if (modify != 0) {
            //数据库token更新成功，更新redis缓存
            //NX: 只在键不存在时，才对键进行设置操作
            //XX: 只在键存在时，才对键进行设置操作
            //EX: 设置键的过期单位为 second 秒
            try {
                if (ParamsConstant.TYPE_TICKET.equals(tokenTicketType)) {
                    redisUtils.set(paramType.getName(), tokenOrTicket, ParamsConstant.TOKEN_TIME_OUT);
                } else {
                    redisUtils.set(ParamsConstant.TOKEN_NOS_KEY_PREFIX + context, tokenOrTicket, ParamsConstant.TOKEN_TIME_OUT);
                }
            } catch (Exception e) {
                log.error("WecomRequestServiceImpl###updateTokenFromWecom设置redis缓存异常");
            }
        }
    }

    private String getContextConstant(String url) {
        return url.substring(0, url.lastIndexOf("?"));
    }

    private String getParam(String url, Map<String, Object> map) {
        String rep;
        int ind;
        for (String key : map.keySet()) {
            if (url.indexOf(key) != 0) {
                ind = url.indexOf(key);
                rep = url.substring(ind + key.length() + 1, ind + key.length() + 5);
                url = url.replace(rep, map.get(key).toString());
            }
        }
        return url;
    }

    private void setZhCorpInfoTicket(ZhCorpInfo zhCorpInfoUpd, String ticket, GenParamEnum paramType) {
        if (AccessCredentialsEnum.UrlParamType.CORP_JSAPI_TICKET.getCode().equals(paramType.getName())) {
            zhCorpInfoUpd.setCorpJsapiTicket(ticket);
            zhCorpInfoUpd.setCorpJsapiTicketModified(new Date());
        } else {
            zhCorpInfoUpd.setAgentJsapiTicket(ticket);
            zhCorpInfoUpd.setAgentJsapiTicketModified(new Date());
        }
    }

    private void setZhCorpInfoToken(ZhCorpInfo zhCorpInfoUpd, String token, GenParamEnum paramType) {
        if (AccessCredentialsEnum.UrlParamType.APPLICATION.getCode().equals(paramType.getName())) {
            zhCorpInfoUpd.setTokenApplication(token);
            zhCorpInfoUpd.setTokenApplicationModified(new Date());
        } else if (AccessCredentialsEnum.UrlParamType.COMMUNICATION.getCode().equals(paramType.getName())) {
            zhCorpInfoUpd.setTokenCommunication(token);
            zhCorpInfoUpd.setTokenCommunicationModified(new Date());
        } else if (AccessCredentialsEnum.UrlParamType.EXTERNAL_CONTACT.getCode().equals(paramType.getName())) {
            zhCorpInfoUpd.setTokenExternalContact(token);
            zhCorpInfoUpd.setTokenExternalContactModified(new Date());
        } else if (AccessCredentialsEnum.UrlParamType.MSG_AUDIT.getCode().equals(paramType.getName())) {
            zhCorpInfoUpd.setTokenMsgAudit(token);
            zhCorpInfoUpd.setTokenMsgAuditModified(new Date());
        }
    }

    private String getRequestQiWeUrl(String methodType, Map<String, Object> body, String urlConstant, String token) {
        if (ParamsConstant.METHOD_GET.equals(methodType)) {
            urlConstant = getParam(urlConstant, body);
        }
        String url = String.format(wecomApiUrlPrefix + urlConstant, token);
        return url;
    }

    private JSONObject doHttpByMethodType(String methodType, Map<String, Object> body, String url) throws Exception {
        if (ParamsConstant.METHOD_POST.equalsIgnoreCase(methodType)) {
            if (Objects.nonNull(body.get("fileStream"))) {
                String httpResult = HttpRequestUtils.httpRequestFileStream(url, String.valueOf(body.get("fileName")), Long.valueOf(String.valueOf(body.get("fileLength"))), (byte[]) body.get("fileStream"));
                log.info("WecomRequestServiceImpl###上传文件###企微API返回: " + httpResult);
                return JSONObject.parseObject(httpResult);
            } else {
                String httpResult = HttpRequestUtils.httpPost(url, JSON.toJSONString(body), 8000);
                log.info("WecomRequestServiceImpl###通用接口###企微API返回: " + httpResult);
                return JSONObject.parseObject(httpResult);
            }
        } else {
            JSONObject httpResult = HttpRequestUtils.getAccessResult(url);
            log.info("WecomRequestServiceImpl###requestQiWeApi###企微API返回: " + httpResult);
            return httpResult;
        }
    }

    private Object createReturnInfo(JSONObject resultObject, String urlConstant) {
        return resultObject;
    }
}
