package tech.wedev.wecom.standard;

import tech.wedev.wecom.entity.po.GenParamPO;
import tech.wedev.wecom.entity.qo.GenParamQO;
import tech.wedev.wecom.enums.GenParamEnum;

import java.util.List;

/**
 * @see GenParamBasicService
 */
@Deprecated
public interface GenParamService extends BaseService<GenParamPO, GenParamQO> {
    String queryWecomGenParamValue(GenParamEnum genParamEnum);

    int updateWecomGenParam(GenParamPO genParamPO);

    /**
     * 读取wecom_gen_param，内部通过Google Guava完成数据缓存
     * @param genParamPO
     * @return
     */
    String queryWecomGenParam(GenParamPO genParamPO);

    String queryWecomGenParam(GenParamQO genParamQO);

    String queryWecomGenParam(GenParamEnum genParamEnum);

    List<GenParamPO> queryWecomGenParamValueByType(String paramType);

}
