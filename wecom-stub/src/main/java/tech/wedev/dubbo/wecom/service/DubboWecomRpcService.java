package tech.wedev.dubbo.wecom.service;

import java.util.Map;

public interface DubboWecomRpcService {
    Map<String, Object> getApiExternalContactGet(Map<String, Object> body);

    Map<String, Object> getApiPushApplicationMessage(Map<String, Object> body);
}
