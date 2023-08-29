package tech.wedev.wecom.entity.po;

import tech.wedev.wecom.tools.ValidatorGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class OrgPOs {

    @Valid
    @NotEmpty(message = "集合不能为空")
    @Size(max = 20, message = "不允许超过最大长度20", groups = ValidatorGroup.Update.class)
    private List<@Valid @NotNull OrgPO> orgPOList;
}
