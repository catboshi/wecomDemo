package tech.wedev.wecom.entity.po;

import tech.wedev.wecom.tools.ValidatorGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class OrgPOList {

    @Valid
    @NotEmpty(message = "机构列表不能为空")
    @Size(max = 20, message = "最多修改20个机构主体信息", groups = ValidatorGroup.Update.class)
    private List<@Valid @NotNull(message = "机构信息不能为空", groups = ValidatorGroup.Update.class) OrgPO> orgPOList;
}
