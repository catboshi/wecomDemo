package tech.wedev.wecom.standard;
import tech.wedev.wecom.entity.po.GenParamBasicPO;
import tech.wedev.wecom.entity.qo.GenParamBasicQO;
import tech.wedev.wecom.enums.GenParamBasicParamCodeEnum;
import tech.wedev.wecom.enums.GenParamBasicParamTypeEnum;


public interface GenParamBasicService extends BasicService<GenParamBasicPO, GenParamBasicQO> {
    String queryWecomGenParamValue(GenParamBasicParamTypeEnum paramParamType, GenParamBasicParamCodeEnum paramCode);

    String queryWecomGenParam(GenParamBasicParamTypeEnum paramType, GenParamBasicParamCodeEnum ParamCode);

    String queryWecomGenParam(GenParamBasicQO genParamBasicQO);
}