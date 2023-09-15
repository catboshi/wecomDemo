package tech.wedev.wecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.wedev.wecom.annos.StopWatch;
import tech.wedev.wecom.entity.po.OrgPO;
import tech.wedev.wecom.entity.qo.CorpInfoQO;
import tech.wedev.wecom.entity.vo.AuthCodeReqVO;
import tech.wedev.wecom.entity.vo.ResponseVO;
import tech.wedev.wecom.exception.ExceptionCode;
import tech.wedev.wecom.standard.CorpInfoMybatisPlusService;
import tech.wedev.wecom.third.WecomRequestService;
import tech.wedev.wecom.tools.ValidatorGroup;
import tech.wedev.wecom.tools.ValidatorTools;
import tech.wedev.wecom.utils.RedisUtils;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private WecomRequestService wecomRequestService;

    @Autowired
    private CorpInfoMybatisPlusService CorpInfoMybatisPlusService;

    @Autowired
    private ValidatorTools validatorTools;

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

    @StopWatch
    @RequestMapping("/externalcontact/get/{external_userid}")
    public Map<String, Object> getExternalContact(@PathVariable String external_userid) {
        return wecomRequestService.externalContactGet("wwbda3d4206748805c",external_userid);
    }

    @StopWatch
    @RequestMapping("/test/aop/stopwatch")
    public String stopWatch() {
        System.out.println("testAopStopWatch");
        return "testAopStopWatch";
    }

    @RequestMapping("/test/jrebel")
    public String testJrebel() {
        System.out.println("testJrebel");
        return "testJrebel";
    }

    @RequestMapping("/test/jrebel/remote")
    public String testJrebelRemote() {
        System.out.println("testJrebelRemote");
        return "testJrebelRemote";
    }

    @RequestMapping("/test/validator/tool")
    public ResponseVO testValidatorTool(@RequestBody @Validated(ValidatorGroup.QywxStatisticsControllerQueryListPageQuery.class) AuthCodeReqVO authVO) {
        /*ResponseVO paramValid = validatorTools.isValid(authVO);*/ //"" -> 失败
        ResponseVO paramValid = validatorTools.isValid(authVO, ValidatorGroup.Select.class);//"" -> 成功
        if (!ExceptionCode.SUCCESS.getCode().equals(paramValid.getRetCode())) {
            return ResponseVO.error(ExceptionCode.INVALID_PARAMETER, paramValid.getData());
        }
        return ResponseVO.success();
    }

    @RequestMapping("/test/validator/customize")
    public ResponseVO testValidatorLiteral(@RequestBody @Validated(ValidatorGroup.Update.class) OrgPO po) {
        return ResponseVO.success();
    }

    @StopWatch
    @RequestMapping(value = { "/a" }, method = {RequestMethod.POST})
    public Object getSignInAuthCode(@Valid @RequestBody AuthCodeReqVO authCodeReqVO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getFieldErrors().stream().map(a -> a.getField() + ": " + a.getDefaultMessage()).reduce((a, b) -> a + " " + b);
        }
        return authCodeReqVO;
    }

    @GetMapping(value = { "/selectCorpInfo" })
    public ResponseVO selectCorpInfo() {
        return ResponseVO.success(CorpInfoMybatisPlusService.select());
    }
    
    @PostMapping(value = { "/updateCorpInfo" })
    public ResponseVO updateCorpInfo(@RequestBody CorpInfoQO corpInfoQO) {
        CorpInfoMybatisPlusService.update(corpInfoQO);
        return ResponseVO.success();
    }
}
