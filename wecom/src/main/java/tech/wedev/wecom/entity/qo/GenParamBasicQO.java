package tech.wedev.wecom.entity.qo;
import tech.wedev.wecom.enums.GenParamBasicParamCodeEnum;
import tech.wedev.wecom.enums.GenParamBasicParamTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tech.wedev.wecom.annos.TableName;
import java.util.Date;
import java.util.List;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("zh_gen_param")
public class GenParamBasicQO extends BasicQO {
    private GenParamBasicParamTypeEnum paramType;

    private GenParamBasicParamCodeEnum paramCode;

    private List<GenParamBasicParamCodeEnum> paramCodes;

    private String paramValue;

    private String paramDesc;

    private List<String> corpIds;
}