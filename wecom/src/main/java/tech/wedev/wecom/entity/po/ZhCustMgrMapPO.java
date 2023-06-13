package tech.wedev.wecom.entity.po;
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
@TableName("zh_cust_mgr_map")
public class ZhCustMgrMapPO extends BasicPO {
    /**
    * 统一认证号
    */
    private String innerMgrId;
    /**
    * 客户经理姓名
    */
    private String innerMgrName;
    /**
    * 机构号
    */
    private String orgCode;
    /**
    * 客户经理ID（企业微信）
    */
    private String qywxMgrId;
    /**
    * 企业微信的企业ID
    */
    private String qywxCorpId;
    /**
    * 客户经理在企业微信的名字
    */
    private String qywxMgrName;
    /**
    * 企业微信的手机号
    */
    private String qywxMgrMobile;
    /**
    * 企业微信职务信息
    */
    private String qywxMgrPosition;
    /**
    * 企业微信邮箱
    */
    private Integer qywxMgrEmail;
    /**
    * 企业微信别名
    */
    private String qywxMgrAlias;
    /**
    * -1-开立失败，1-已激活，2-已禁用，4-未激活，5-退出企业，-2-待开立
    */
    private Integer qywxMgrStatus;
    /**
    * 企业微信中的地址
    */
    private String qywxMgrAddress;
    /**
    * 是否冻结状态 1: 启用，0: 禁用
    */
    private Integer enable;
    /**
    * 冻结原因（所属机构变更，在职状态变更）
    */
    private String enableReason;
    /**
    * 企微中所属部门id列表
    */
    private String department;
    /**
    * 企微中主部门
    */
    private String mainDepartment;
    /**
    * 条线名称
    */
    private String businessLineName;
    /**
    * 条线标签
    */
    private String businessLineTag;
    /**
    * 条线所属机构
    */
    private String businessLineOrg;
}