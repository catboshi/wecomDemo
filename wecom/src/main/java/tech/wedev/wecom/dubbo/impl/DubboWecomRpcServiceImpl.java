package tech.wedev.wecom.dubbo.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.wedev.dubbo.wecom.service.DubboWecomRpcService;
import tech.wedev.wecom.exception.WecomException;
import tech.wedev.wecom.third.WecomRequestService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("dubboWecomService")
public class DubboWecomRpcServiceImpl implements DubboWecomRpcService {

    @Autowired
    private WecomRequestService wecomRequestService;

    @Override
    public Map<String, Object> getApiExternalContactGet(Map<String, Object> body) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> requestMap = MapUtils.getMap(body, "Private");
        String externalUserId = MapUtils.getString(requestMap, "externalUserId");
        String corpId = MapUtils.getString(requestMap, "corpId");
        log.info("DubboWecomRpcServiceImpl.getApiExternalContactGet###企微API入参: \"corpId\": " + corpId + ", \"externalUserId\": " + externalUserId);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("external_userid", externalUserId);
        try {
            resultMap.put("data", JSON.toJSON(wecomRequestService.externalContactGet(corpId, externalUserId)));
            resultMap.put("errCode", 0);
            resultMap.put("errMsg", "ok");
        } catch (WecomException wecomException) {
            resultMap.put("errCode", wecomException.getCode());
            resultMap.put("errMsg", wecomException.getMsg());
        } catch (Exception e) {
            log.error("DubboWecomRpcService服务异常", e);
            resultMap.put("errCode", 500);
            resultMap.put("errMsg", "未知错误Exception: " + e.getMessage());
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getApiPushApplicationMessage(Map<String, Object> body) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap.put("data", JSON.toJSON(wecomRequestService.sendApplicationMessage(body)));
            resultMap.put("errCode", 0);
            resultMap.put("errMsg", "ok");
        } catch (WecomException wecomException) {
            resultMap.put("errCode", wecomException.getCode());
            resultMap.put("errMsg", wecomException.getMsg());
        } catch (Exception e) {
            log.error("DubboWecomRpcService服务异常", e);
            resultMap.put("errCode", 500);
            resultMap.put("errMsg", "未知错误Exception: " + e.getMessage());
        }
        return resultMap;
    }
}
