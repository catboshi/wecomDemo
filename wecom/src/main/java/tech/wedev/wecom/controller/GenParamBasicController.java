package tech.wedev.wecom.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import tech.wedev.wecom.standard.GenParamBasicService;
import tech.wedev.wecom.standard.BasicService;
import tech.wedev.wecom.entity.po . GenParamBasicPO;
import tech.wedev.wecom.entity.qo.GenParamBasicQO;


@RestController
@RequestMapping("zh_gen_param_basic")
@Slf4j
public class GenParamBasicController extends BasicController<GenParamBasicPO, GenParamBasicQO> {
    @Autowired
    private GenParamBasicService genParamBasicService;
    @Override
    public BasicService<GenParamBasicPO,GenParamBasicQO> getBasicService(){
        return genParamBasicService;
    }
}