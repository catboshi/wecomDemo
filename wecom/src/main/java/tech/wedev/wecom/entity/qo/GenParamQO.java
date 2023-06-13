package tech.wedev.wecom.entity.qo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GenParamQO extends BaseQO {
    String paramType;
    String paramCode;
    String paramValue;
    String paramDesc;
    private List<String> corpIds;
}
