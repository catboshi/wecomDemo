package tech.wedev.wecom.third;

import tech.wedev.wecom.entity.bo.AccessCredentialsCommand;

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
}
