package tech.wedev.wecom.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.wedev.wecom.annos.StopWatch;
import tech.wedev.wecom.third.WecomRequestService;
import tech.wedev.wecom.utils.RedisUtils;

import java.util.Map;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private WecomRequestService wecomRequestService;

    @RequestMapping("/redis/set")
    public String setRedis() {
        redisUtils.set("key", "A value is set ok");
        return "ok";
    }

    @RequestMapping("/redis/get")
    public String getRedis() {
        return (String) redisUtils.get("key");
    }

    @StopWatch
    @RequestMapping("/externalcontact/get/{external_userid}")
    public Map<String, Object> getExternalContact(@PathVariable String external_userid) {
        return wecomRequestService.externalContactGet("wwbda3d4206748805c",external_userid);
    }

    @RequestMapping("/jrebel")
    public String testJrebel() {
        System.out.println("testJrebel");
        return "testJrebel ok";
    }

    @RequestMapping("/jrebel/remote")
    public String testJrebelRemote() {
        System.out.println("testJrebelRemote");
        return "testJrebelRemote ok";
    }
}
