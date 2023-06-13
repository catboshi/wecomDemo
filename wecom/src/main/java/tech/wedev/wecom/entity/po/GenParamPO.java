package tech.wedev.wecom.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GenParamPO extends BasePO{
    String paramType;
    String paramCode;
    String paramValue;
    String paramDesc;
}
