package tech.wedev.wecom.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import tech.wedev.wecom.annos.TableName;
import tech.wedev.wecom.annos.Validator;
import tech.wedev.wecom.tools.ValidatorGroup;

import javax.validation.constraints.NotBlank;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("zh_org")
public class OrgPO extends BasicPO {
    /**
    * 机构编号
    */
    @NotBlank(message = "${code}字段不能为空", groups = ValidatorGroup.Update.class)
    @Length(max = 1, message = "${code}字段长度过长，最大值为1", groups = {ValidatorGroup.Insert.class, ValidatorGroup.Update.class})
    @Validator(strValues = {"0", "1"}, message = "${code}只能是0或者1", groups = {ValidatorGroup.Insert.class, ValidatorGroup.Update.class})
    private String code;
    /**
    * 父级机构编号
    */
    private String parentCode;
    /**
    * 机构名称
    */
    private String name;
    /**
    * 机构层级
    */
    private Integer level;
    /**
    * 机构类型
    */
    private Integer type;
    /**
    * 分支机构类型
    */
    private Integer sign;
    /**
    * 机构排序
    */
    private Integer sort;
    /**
    * 企微中的部门id
    */
    private String qywxDepartment;
}