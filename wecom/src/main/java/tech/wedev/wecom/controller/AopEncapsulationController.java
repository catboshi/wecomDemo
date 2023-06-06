package tech.wedev.wecom.controller;

import tech.wedev.wecom.annos.StopWatch;
import tech.wedev.wecom.entity.vo.AuthCodeReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/aop")
public class AopEncapsulationController {

    @StopWatch
    @RequestMapping(value = { "/a" }, method = {RequestMethod.POST})
    public Object getSignInAuthCode(@Valid @RequestBody AuthCodeReq authCodeReq, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getFieldErrors().stream().map(a->a.getField()+": "+a.getDefaultMessage()).reduce((a,b)->a+" "+b);
        }
        return authCodeReq;
    }
}
