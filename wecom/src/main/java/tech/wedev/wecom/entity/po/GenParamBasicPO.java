package tech.wedev.wecom.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tech.wedev.wecom.annos.TableName;
import tech.wedev.wecom.enums.GenParamBasicParamCodeEnum;
import tech.wedev.wecom.enums.GenParamBasicParamTypeEnum;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("zh_gen_param")
public class GenParamBasicPO extends BasicPO {
    private String paramValue;

    private String paramDesc;

    private GenParamBasicParamTypeEnum paramType;

    private GenParamBasicParamCodeEnum paramCode;
}
