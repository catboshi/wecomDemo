package tech.wedev.wecom.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.wedev.wecom.annos.StopWatch;
import tech.wedev.wecom.entity.po.CorpInfo;
import tech.wedev.wecom.entity.po.CustMgrMapPO;
import tech.wedev.wecom.entity.po.OpLogPO;
import tech.wedev.wecom.entity.vo.ResponseVO;
import tech.wedev.wecom.enums.OpTypeEnum;
import tech.wedev.wecom.mybatis.mapper.OpLogMapper;
import tech.wedev.wecom.standard.ClientMsgReadLogService;
import tech.wedev.wecom.standard.CorpInfoMybatisPlusService;
import tech.wedev.wecom.standard.CustMgrMapService;
import tech.wedev.wecom.third.WecomRequestService;
import tech.wedev.wecom.utils.SpringRedisUtil;

import java.util.Date;
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
    private OpLogMapper opLogMapper;

    @Autowired
    ClientMsgReadLogService clientMsgReadLogService;

    @RequestMapping("/redis/set")
    public String setRedis() {
        springRedisUtil.set("key", "A value is set ok");
        return "ok";
    }

    @RequestMapping("/redis/get")
    public String getRedis() {
        return (String) springRedisUtil.get("key");
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
//        OpLogPO opLogPO = OpLogPO.builder()
//                .opUserId("user1705209132")
//                .opTellerNo("555114100")
//                .isDeleted(0)
//                .createId(0L)
//                .gmtCreate(new Date())
//                .gmtModified(new Date())
//                .modifiedId(0L)
//                .opType(OpTypeEnum.READ_MSG.getCode())
//                .corpId("测试租户")
//                .orgCode("测试机构")
//                .opContent(JSON.toJSONString(log)).build();
//        opLogMapper.saveLog(opLogPO);

        clientMsgReadLogService.deleteOpContentAfterProcess(ImmutableList.of("4500306879"));
        return ResponseVO.success();
    }
}
