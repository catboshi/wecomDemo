package tech.wedev.wecom.entity.qo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tech.wedev.wecom.annos.TableName;
import java.util.Date;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("zh_org")
public class ZhOrgQO extends BasicQO {
    /**
    * 机构编号
    */
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