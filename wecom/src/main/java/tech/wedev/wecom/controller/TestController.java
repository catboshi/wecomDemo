package tech.wedev.wecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.wedev.wecom.third.WecomRequestService;
import tech.wedev.wecom.utils.RedisUtils;

import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private WecomRequestService wecomRequestService;

    @RequestMapping("/redis/set")
    public String setRedis() {
        redisUtils.set("ringer", "A value is set ok");
        return "true";
    }

    @RequestMapping("/redis/get")
    public String getRedis() {
        String result = (String) redisUtils.get("ringer");
        return result;
    }

    @RequestMapping("/externalcontact/get/{external_userid}")
    public Map<String, Object> getExternalContact(@PathVariable String external_userid) {
        return wecomRequestService.externalContactGet("wwbda3d4206748805c",external_userid);
    }
}
