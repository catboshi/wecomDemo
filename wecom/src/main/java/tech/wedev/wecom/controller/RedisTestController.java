package tech.wedev.wecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.wedev.wecom.utils.RedisUtils;

@RestController
public class RedisTestController {

    @Autowired
    private RedisUtils redisUtils;

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
}
