package tech.wedev.wecom.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.wedev.wecom.annos.StopWatch;
import tech.wedev.wecom.entity.common.User;
import tech.wedev.wecom.entity.po.CorpInfo;
import tech.wedev.wecom.entity.po.CustMgrMapPO;
import tech.wedev.wecom.entity.vo.ResponseVO;
import tech.wedev.wecom.mybatis.mapper.OpLogMapper;
import tech.wedev.wecom.standard.ClientMsgReadLogService;
import tech.wedev.wecom.standard.CorpInfoMybatisPlusService;
import tech.wedev.wecom.standard.CustMgrMapService;
import tech.wedev.wecom.third.WecomRequestService;
import tech.wedev.wecom.utils.JWTUtil;
import tech.wedev.wecom.utils.SpringRedisUtil;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private SpringRedisUtil springRedisUtil;

    @Autowired
    private WecomRequestService wecomRequestService;

    @Autowired
    private CustMgrMapService custMgrMapService;

    @Autowired
    private CorpInfoMybatisPlusService corpInfoMybatisPlusService;

    @Autowired
    ClientMsgReadLogService clientMsgReadLogService;

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping("/redis/set")
    public String setRedis() {
        springRedisUtil.hset("USER:DETAIL", "glizark", User.builder().userid("glizark").build());
        return "ok";
    }

    @RequestMapping("/redis/get")
    public Object getRedis() {
        return springRedisUtil.hget("USER:DETAIL", "glizark");
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

    @RequestMapping("/page")
    public PageInfo<CustMgrMapPO> testPage() {
        List<CustMgrMapPO> custMgrMapList = custMgrMapService.selectList();
        return PageInfo.of(custMgrMapList);
    }

    @RequestMapping("/mybatis-plus")
    public PageInfo<CorpInfo> testMybatisPlus() {
        List<CorpInfo> corpInfoMybatisPlusList = corpInfoMybatisPlusService.select();
        return PageInfo.of(corpInfoMybatisPlusList);
    }

    @RequestMapping("/clob")
    public ResponseVO testClob() {
        clientMsgReadLogService.deleteOpContentAfterProcess(ImmutableList.of("4500306879"));
        return ResponseVO.success();
    }

    @GetMapping("/generateToken")
    public String generateToken(@RequestParam("code") String code) {
        return jwtUtil.generateToken(code);
    }
}
