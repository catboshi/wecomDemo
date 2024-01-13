package tech.wedev.wecom.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import tech.wedev.wecom.entity.po.GenParamPO;
import tech.wedev.wecom.entity.qo.GenParamQO;
import tech.wedev.wecom.enums.GenParamEnum;

import java.util.List;

/**
 * @see GenParamBasicMapper
 */
@Deprecated
public interface GenParamMapper extends BaseMapper<GenParamPO, GenParamQO> {
    int updateWecomGenParam(GenParamQO genParamQO);

    String selectValueByUK(@Param("genParamEnum") GenParamEnum genParamEnum, @Param("corpId") String corpId);

    List<GenParamPO> selectByParam(@Param("wecomGenParam") GenParamPO wecomGenParam);

    List<GenParamQO> selectByParams(@Param("wecomGenParam") GenParamQO wecomGenParam);

    List<GenParamPO> selectByParamType(@Param("wecomGenParam") GenParamQO wecomGenParam);

}
