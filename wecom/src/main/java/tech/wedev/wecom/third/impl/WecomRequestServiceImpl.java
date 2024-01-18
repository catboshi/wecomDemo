package tech.wedev.wecom.third.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tech.wedev.wecom.constants.ParamsConstant;
import tech.wedev.wecom.constants.WecomApiUrlConstant;
import tech.wedev.wecom.context.TokenContextHolder;
import tech.wedev.wecom.mybatis.mapper.CorpInfoMapper;
import tech.wedev.wecom.entity.bo.AccessCredentialsCommand;
import tech.wedev.wecom.entity.po.GenParamBasicPO;
import tech.wedev.wecom.entity.po.GenParamPO;
import tech.wedev.wecom.entity.po.CorpInfo;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@PropertySource(value = "classpath:META-INF/global-config.properties")
public class WecomRequestServiceImpl implements WecomRequestService {

    @Autowired
    private GenParamService genParamService;
    @Autowired
    private GenParamBasicService genParamBasicService;

    @Autowired
    private CorpInfoMapper corpInfoMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private SpringRedisUtil springRedisUtil;

    @Autowired
    ThreadPoolTaskExecutor asyncTaskExecutor;

    @Value("${wecom.api.url.prefix}")
    private String wecomApiUrlPrefix;

    @Override
    public Object generalCallQiWeApi(String corpId, String methodType, Map<String, Object> body, String urlConstant) {
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

        JSONObject resultObject;
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
            this.refreshTokenFromCorpInfo(corpId, getContextConstant(urlConstant));
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
        String tokenKey = Joiner.on("_").join(corpId, ParamsConstant.TOKEN_CACHE_KEY, command.getInterfaceIdentifyUrl());

        token = (String) springRedisUtil.get(tokenKey);
        if (StringUtil.isNotBlank(token)) {
            //缓存中存在token，直接返回
            log.info("WecomRequestServiceImpl###generateAccessToken###redis缓存token: " + token);
            return token;
        }

        //redis缓存token不存在或redis异常，从数据库读取token
        GenParamEnum paramType = this.paramTypeMatch(interfaceIdentifyUrl);
        log.info("WecomRequestServiceImpl###generateAccessToken###匹配类型: " + paramType);
        List<CorpInfo> tokenInDB = corpInfoMapper.findByCorpId(corpId);
        //数据库存在token且token有效，直接返回
        if (tokenInDB == null || tokenInDB.isEmpty()) {
            log.error("WecomRequestServiceImpl###generateAccessToken###corpInfo异常，无该corpId相关配置");
            return null;
        }

        int tokenTimeOut = ParamsConstant.TOKEN_TIME_OUT;
        try {
            Date tokenModified = findTokenModified(paramType, tokenInDB.get(0));
            if (!Objects.isNull(tokenModified)) {
                tokenTimeOut = Integer.parseInt(DateUtil.printDiffer(tokenModified, DateTime.now()));
            }
        } catch (Exception e) {
            log.error("WecomRequestServiceImpl###generateAccessToken###parse tokenTimeOut异常: ", e);
        }

        if (!CollectionUtils.isEmpty(tokenInDB) && tokenTimeOut < ParamsConstant.TOKEN_TIME_OUT
                && StringUtil.isNotEmpty(findTokenFromCorpInfoDB(paramType, tokenInDB.get(0)))) {
            log.info("WecomRequestServiceImpl###generateAccessToken###数据库缓存token:" + JSONObject.toJSONString(tokenInDB.get(0)));
            return findTokenFromCorpInfoDB(paramType, tokenInDB.get(0));
        }

        //数据库token不存在或失效，重新获取token
        token = this.getTokenFromQiweApi(paramType, tokenInDB.get(0));
        log.info("WecomRequestServiceImpl###generateAccessToken###新token: " + token);

        //更新数据库token，设置redis缓存
        updateDbTokenOrTicket(paramType, ParamsConstant.TYPE_TOKEN, token, corpId, interfaceIdentifyUrl);
        return token;
    }

    @Override
    public JSONObject authGetUserInfo(String code) {
        log.info("WecomRequestServiceImpl###authGetUserInfo###企微API入参: " + code);
        //获取请求的token
        AccessCredentialsCommand accessTokenCommand = AccessCredentialsCommand.builder().corpId("System")
                .interfaceIdentifyUrl(getContextConstant(WecomApiUrlConstant.AUTH_GET_USER_INFO)).build();
        String token = this.generateAccessToken(accessTokenCommand);
        String url = String.format(wecomApiUrlPrefix + WecomApiUrlConstant.AUTH_GET_USER_INFO, token, code);
        log.info("WecomRequestServiceImpl###authGetUserInfo###企微API URL: " + url);
        JSONObject httpResult;
        try {
            httpResult = HttpRequestUtil.getAccessResult(url);
        } catch (Exception e) {
            log.error("WecomRequestServiceImpl###authGetUserInfo###企微API调用异常" + e.getMessage());
            throw new WecomException(ExceptionCode.REQUEST_ERROR);
        }
        log.info("WecomRequestServiceImpl###authGetUserInfo###企微API返回: " + JSON.toJSONString(httpResult));
        int errCode = httpResult.getIntValue("errcode");
        if (errCode == 0) {
            return httpResult;
        } else if (errCode == 42001) {
            //token失效，重新调用获取token
            this.refreshTokenFromCorpInfo("System", getContextConstant(WecomApiUrlConstant.AUTH_GET_USER_INFO));
            return this.authGetUserInfo(code);
        } else {
            throw new WecomException(errCode, "调用企微API返回错误");
        }
    }

    @Override
    public Map<String, Object> externalContactGet(String corpId, String externalUserId) {
        log.info("WecomRequestServiceImpl.externalContactGet###企微API入参: \"corpId\": " + corpId + ", \"externalUserId\": " + externalUserId);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("external_userid", externalUserId);
        return ((JSONObject) generalCallQiWeApi(corpId, ParamsConstant.METHOD_GET, requestBody, WecomApiUrlConstant.EXTERNAL_CONTACT_GET)).getInnerMap();
    }

    @Override
    public Map<String, Object> externalContactGetList(String corpId, List<String> externalUserIdList) {
        log.info("WecomRequestServiceImpl.externalContactGet###企微API入参: \"corpId\": " + corpId + ", \"externalUserId\": " + JSON.toJSONString(externalUserIdList));
        HashMap<@Nullable String, @Nullable Object> resultMap = Maps.newHashMap();
        CompletableFuture<Void> futures = CompletableFuture.allOf(externalUserIdList.stream()
                .map(externalUserId -> CompletableFuture.supplyAsync(() -> invokeExternalContact(corpId, externalUserId), asyncTaskExecutor)
                        .whenComplete((v, e) -> {
                            if (e == null) {
                                resultMap.putAll(v);
                            } else {
                                log.error("企微外部用户ID：{}，调用获取企微用户详情接口异常：", externalUserId, e);
                                resultMap.putIfAbsent(externalUserId, "企微API调用异常，请稍后重试");
                            }
                        })).toArray(CompletableFuture[]::new));

        futures.whenComplete((v, e) -> {
            if (e == null) {
                log.info("调用获取企微用户详情接口成功{}", JSON.toJSONString(resultMap));
            } else {
                log.error("调用获取企微用户详情接口失败");
            }
        });
        //获取结果上限时间
        try {
            futures.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            return ImmutableMap.of("errMsg", "获取企微用户详情任务结果超时，请稍后重试");
        } catch (Exception ex) {
            log.error("获取企微用户详情任务结果异常：", ex);
            return ImmutableMap.of("errMsg", "获取企微用户详情任务结果异常，请稍后重试");
        }
        return resultMap;
    }

    private Map<String, Object> invokeExternalContact(String corpId, String externalUserId) {
        return ((JSONObject) generalCallQiWeApi(corpId, ParamsConstant.METHOD_GET, ImmutableMap.of("external_userid", externalUserId), WecomApiUrlConstant.EXTERNAL_CONTACT_GET)).getInnerMap();
    }

    @Override
    public Map<String, Object> sendApplicationMessage(Map<String, Object> requestMap) {
        log.info("WecomRequestServiceImpl.sendWelcomeMessage###企微API入参: " + JSONObject.toJSONString(requestMap));
        Map<String, Object> publicBody = MapUtils.getMap(requestMap, "Public");
        Map<String, Object> privateBody = MapUtils.getMap(requestMap, "Private");
        String corpId = MapUtils.getString(publicBody, "corpId");
        return ((JSONObject) generalCallQiWeApi(corpId, ParamsConstant.METHOD_POST, privateBody, WecomApiUrlConstant.SEND_MESSAGE_APIURL)).getInnerMap();
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
     * 根据paramType匹配CorpInfo对应Token值
     *
     * @param paramType
     * @param corpInfo
     * @return
     */
    private String findTokenFromCorpInfoDB(GenParamEnum paramType, CorpInfo corpInfo) {
        if (AccessCredentialsEnum.UrlParamType.APPLICATION.getCode().equals(paramType.getName())) {
            return corpInfo.getTokenApplication();
        } else if (AccessCredentialsEnum.UrlParamType.MSG_AUDIT.getCode().equals(paramType.getName())) {
            return corpInfo.getTokenMsgAudit();
        } else if (AccessCredentialsEnum.UrlParamType.COMMUNICATION.getCode().equals(paramType.getName())) {
            return corpInfo.getTokenCommunication();
        } else if (AccessCredentialsEnum.UrlParamType.EXTERNAL_CONTACT.getCode().equals(paramType.getName())) {
            return corpInfo.getTokenExternalContact();
        }
        return "";
    }

    /**
     * 根据paramType匹配CorpInfo对应Secret值
     * @param paramType
     * @param corpInfo
     * @return
     */
    private String findSecretFromCorpInfoDB(GenParamEnum paramType, CorpInfo corpInfo) {
        if (AccessCredentialsEnum.UrlParamType.APPLICATION.getCode().equals(paramType.getName())) {
            return corpInfo.getSecretApplication();
        } else if (AccessCredentialsEnum.UrlParamType.MSG_AUDIT.getCode().equals(paramType.getName())) {
            return corpInfo.getSecretMsgAudit();
        } else if (AccessCredentialsEnum.UrlParamType.COMMUNICATION.getCode().equals(paramType.getName())) {
            return corpInfo.getSecretCommunication();
        } else if (AccessCredentialsEnum.UrlParamType.EXTERNAL_CONTACT.getCode().equals(paramType.getName())) {
            return corpInfo.getSecretExternalContact();
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
            JSONObject accessToken = HttpRequestUtil.getAccessResult(String.format(url, corpIdInDB.get(0).getParamValue(), secretInDB.get(0).getParamValue()));
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

    private String getTokenFromQiweApi(GenParamEnum paramType, CorpInfo corpInfo) {
        //查询secret参数
        String secretInDB = this.findSecretFromCorpInfoDB(paramType, corpInfo);
        log.info("WecomRequestServiceImpl###getTokenFromQiweApi###SECRET: " + secretInDB);
        ExceptionAssert.isTrue(secretInDB.isEmpty(), ExceptionCode.PARAMETER_SECRET_ERROR);

        String corpIdInDB = corpInfo.getCorpId();
        //调用企微API
        String url = wecomApiUrlPrefix + WecomApiUrlConstant.GET_TOKEN;
        String token;
        try {
            JSONObject accessToken = HttpRequestUtil.getAccessResult(String.format(url, corpIdInDB, secretInDB));
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

    private Date findTokenModified(GenParamEnum paramType, CorpInfo corpInfo) {
        if (AccessCredentialsEnum.UrlParamType.APPLICATION.getCode().equals(paramType.getName())) {
            return corpInfo.getTokenApplicationModified();
        } else if (AccessCredentialsEnum.UrlParamType.MSG_AUDIT.getCode().equals(paramType.getName())) {
            return corpInfo.getTokenMsgAuditModified();
        } else if (AccessCredentialsEnum.UrlParamType.COMMUNICATION.getCode().equals(paramType.getName())) {
            return corpInfo.getTokenCommunicationModified();
        } else if (AccessCredentialsEnum.UrlParamType.EXTERNAL_CONTACT.getCode().equals(paramType.getName())) {
            return corpInfo.getTokenExternalContactModified();
        } else if (AccessCredentialsEnum.UrlParamType.AGENT_JSAPI_TICKET.getCode().equals(paramType.getName())) {
            return corpInfo.getAgentJsapiTicketModified();
        } else if (AccessCredentialsEnum.UrlParamType.CORP_JSAPI_TICKET.getCode().equals(paramType.getName())) {
            return corpInfo.getCorpJsapiTicketModified();
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
    private String refreshTokenFromCorpInfo(String corpId, String interfaceIdentifyUrl) {
        log.info(interfaceIdentifyUrl + "###access_token失败，重新获取");
        //根据URL获取API类型
        GenParamEnum paramType = this.paramTypeMatch(interfaceIdentifyUrl);
        //调用企微API获取TOKEN
        AccessCredentialsCommand accessTokenCommand = AccessCredentialsCommand.builder().corpId(corpId)
                .interfaceIdentifyUrl(interfaceIdentifyUrl).build();
        log.info("WecomRequestServiceImpl###refreshTokenFromCorpInfo###accessTokenCommand info: " + JSONObject.toJSONString(accessTokenCommand));
        String token = this.generateAccessToken(accessTokenCommand);
        List<CorpInfo> tokenInDB = corpInfoMapper.findByCorpId(corpId);
        ExceptionAssert.isTrue(CollectionUtils.isEmpty(tokenInDB), ExceptionCode.PARAMETER_CORP_ID_ERROR);


        //更新数据库token，设置redis缓存
        this.updateDbTokenOrTicket(paramType, ParamsConstant.TYPE_TOKEN, token, corpId, interfaceIdentifyUrl);
        return token;
    }


    /**
     * token更新后更新参数表和corpInfo表token
     *
     * @param paramType
     * @param tokenTicketType
     * @param tokenOrTicket
     * @param corpId
     * @param context
     */
    private void updateDbTokenOrTicket(GenParamEnum paramType, String tokenTicketType, String tokenOrTicket, String corpId, String context) {
        //更新数据token，设置redis缓存
        List<CorpInfo> tokenInDB = corpInfoMapper.findByCorpId(corpId);
        this.updateTokenFromCorpInfo(paramType, tokenTicketType, tokenOrTicket, tokenInDB.get(0), context);

        //更新参数表token，设置redis缓存
//        List<GenParamPO> tokenInDBParam = this.getParamFromDB(tokenTicketType, paramType);
        List<GenParamBasicPO> tokenInDBParam = genParamBasicService.select(GenParamBasicQO.builder().isDeleted(BaseDeletedEnum.EXISTS).paramType(EnumUtil.getByStringCode(GenParamBasicParamTypeEnum.class, paramType.getParamTypeEnum().getName()))
                .paramCode(EnumUtil.getByStringCode(GenParamBasicParamCodeEnum.class, paramType.getName())).orderBys(ArrayUtil.asArrayList("id_0")).corpIds(ArrayUtil.asArrayNotNull(corpId, "SYSTEM")).build());

        this.updateTokenFromWecom(corpId, paramType, tokenTicketType, tokenOrTicket, tokenInDBParam, context);
    }

    /**
     * 更新数据库，redis缓存TOKEN
     * 多租户
     *
     * @param paramType
     * @param tokenTicketType
     * @param tokenOrTicket
     * @param corpInfo
     * @param interfaceIdentifyUrl
     */
    private void updateTokenFromCorpInfo(GenParamEnum paramType, String tokenTicketType, String tokenOrTicket, CorpInfo corpInfo,
                                           String interfaceIdentifyUrl) {
        String corpId = corpInfo.getCorpId();

        CorpInfo corpInfoUpd = new CorpInfo();
        corpInfoUpd.setId(corpInfo.getId());
        if (ParamsConstant.TYPE_TICKET.equals(tokenTicketType)) {
            //更新对应ticket字段值
            setCorpInfoTicket(corpInfoUpd, tokenOrTicket, paramType);
        } else {
            //更新对应token字段值
            setCorpInfoToken(corpInfoUpd, tokenOrTicket, paramType);
        }
        corpInfoUpd.setGmtModified(new Date());
        corpInfoUpd.setCorpId(corpInfo.getCorpId());
        corpInfoUpd.setModifiedId(0L);
        int modify = corpInfoMapper.updateByPrimaryKeySelective(corpInfoUpd);

        if (modify != 0) {
            //数据库token更新成功，更新redis缓存
            //NX: 只在键不存在时，才对键进行设置操作
            //XX: 只在键存在时，才对键进行设置操作
            //EX: 设置键的过期单位为 second 秒
            try {
                if (ParamsConstant.TYPE_TICKET.equals(tokenTicketType)) {
                    springRedisUtil.set(corpId + ParamsConstant.CACHE_KEY_SEPARATE + paramType.getName(), tokenOrTicket, ParamsConstant.TOKEN_TIME_OUT);
                } else {
                    String tokenKey = Joiner.on(ParamsConstant.CACHE_KEY_SEPARATE).join(corpId, ParamsConstant.TOKEN_CACHE_KEY, interfaceIdentifyUrl);
                    springRedisUtil.set(tokenKey, tokenOrTicket, ParamsConstant.TOKEN_TIME_OUT);
                }
            } catch (Exception e) {
                log.error("WecomRequestServiceImpl###updateTokenFromCorpInfo###设置redis缓存异常");
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
                    springRedisUtil.set(paramType.getName(), tokenOrTicket, ParamsConstant.TOKEN_TIME_OUT);
                } else {
                    springRedisUtil.set(ParamsConstant.TOKEN_CACHE_KEY_PREFIX + context, tokenOrTicket, ParamsConstant.TOKEN_TIME_OUT);
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

    private void setCorpInfoTicket(CorpInfo corpInfoUpd, String ticket, GenParamEnum paramType) {
        if (AccessCredentialsEnum.UrlParamType.CORP_JSAPI_TICKET.getCode().equals(paramType.getName())) {
            corpInfoUpd.setCorpJsapiTicket(ticket);
            corpInfoUpd.setCorpJsapiTicketModified(new Date());
        } else {
            corpInfoUpd.setAgentJsapiTicket(ticket);
            corpInfoUpd.setAgentJsapiTicketModified(new Date());
        }
    }

    private void setCorpInfoToken(CorpInfo corpInfoUpd, String token, GenParamEnum paramType) {
        if (AccessCredentialsEnum.UrlParamType.APPLICATION.getCode().equals(paramType.getName())) {
            corpInfoUpd.setTokenApplication(token);
            corpInfoUpd.setTokenApplicationModified(new Date());
        } else if (AccessCredentialsEnum.UrlParamType.COMMUNICATION.getCode().equals(paramType.getName())) {
            corpInfoUpd.setTokenCommunication(token);
            corpInfoUpd.setTokenCommunicationModified(new Date());
        } else if (AccessCredentialsEnum.UrlParamType.EXTERNAL_CONTACT.getCode().equals(paramType.getName())) {
            corpInfoUpd.setTokenExternalContact(token);
            corpInfoUpd.setTokenExternalContactModified(new Date());
        } else if (AccessCredentialsEnum.UrlParamType.MSG_AUDIT.getCode().equals(paramType.getName())) {
            corpInfoUpd.setTokenMsgAudit(token);
            corpInfoUpd.setTokenMsgAuditModified(new Date());
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
                String httpResult = HttpRequestUtil.httpRequestFileStream(url, String.valueOf(body.get("fileName")), Long.valueOf(String.valueOf(body.get("fileLength"))), (byte[]) body.get("fileStream"));
                log.info("WecomRequestServiceImpl###上传文件###企微API返回: " + httpResult);
                return JSONObject.parseObject(httpResult);
            } else {
                String httpResult = HttpRequestUtil.httpPost(url, JSON.toJSONString(body), 8000);
                log.info("WecomRequestServiceImpl###通用接口###企微API返回: " + httpResult);
                return JSONObject.parseObject(httpResult);
            }
        } else {
            JSONObject httpResult = HttpRequestUtil.getAccessResult(url);
            log.info("WecomRequestServiceImpl###requestQiWeApi###企微API返回: " + httpResult);
            return httpResult;
        }
    }

    private Object createReturnInfo(JSONObject resultObject, String urlConstant) {
        return resultObject;
    }
}
