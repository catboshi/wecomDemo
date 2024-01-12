package tech.wedev.wecom.third;

import tech.wedev.wecom.entity.bo.AccessCredentialsCommand;

import java.util.List;
import java.util.Map;

public interface WecomRequestService {
    /**
     * 通用API服务请求封装
     * 如果是文件类型，body参数传参方式如下：文件名fileName（带后缀），文件长度fileLength，文件流fileStream
     * @param corpId
     * @param methodType
     * @param body
     * @param urlConstant
     * @return
     */
    Object generalCallQiWeApi(String corpId, String methodType, Map<String, Object> body, String urlConstant);

    /**
     * 根据通用API的URL，自动获取响应TOKEN
     * 企微API接口交互支持多租户
     * @param command
     * @return
     */
    String generateAccessToken(AccessCredentialsCommand command);

    /**
     * 身份验证-网页授权登录-获取访问用户身份
     * @param code
     * @return 参考企业微信官方文档
     */
    Map<String, Object> authGetUserInfo(String code);

    /**
     * 客户联系-客户管理-获取客户详情
     * @param externalUserId
     * @param corpId
     * @return 参考企业微信官方文档
     */
    Map<String, Object> externalContactGet(String corpId, String externalUserId);


    Map<String, Object> externalContactGetList(String corpId, List<String> externalUserId);

    /**
     * 消息推送-发送应用消息
     * @param requestMap
     * @return 参考企业微信官方文档
     */
    Map<String, Object> sendApplicationMessage(Map<String, Object> requestMap);
}
