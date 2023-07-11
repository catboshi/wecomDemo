package tech.wedev.wecom.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tech.wedev.wecom.annos.TableName;
import tech.wedev.wecom.enums.QywxContactConfigInfoQrTypeEnum;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("zh_qywx_contact_config_info")
public class QywxContactConfigInfoPO extends BasicPO {
    /**
    * 二维码ID，Snowflake生成
    */
    private String qrId;
    /**
    * 配置ID
    */
    private String configId;
    /**
    * 应用名
    */
    private String appName;
    /**
    * 接入主体：1.总公司，2。分公司
    */
    private String appFlag;
    /**
    * 二维码类型：1是渠道活码，2是一客一码，3是备注码
    */
    private QywxContactConfigInfoQrTypeEnum qrType;
    /**
    * 渠道名称
    */
    private String channelName;
    /**
    * 客户经理类型，1-个人，2-对公
    */
    private Integer innerMgrType;
    /**
    * 客户经理统一认证号
    */
    private String innerMgrId;
    /**
    * 客户cis号
    */
    private String innerCustId;
    /**
    * 账户号，对公客户必输
    */
    private String custAccount;
    /**
    * 客户名称（对公客户为企业名称；个人客户为姓名）
    */
    private String custName;
    /**
    * 客户手机号
    */
    private String custMobile;
    /**
    * 对公客户联系人类型
    */
    private String contactCustRole;
    /**
    * 对公客户联系人姓名
    */
    private String contactCustName;
    /**
    * 推送链接
    */
    private String linkUrl;
    /**
    * 推送链接类型，1-H5，2-小程序
    */
    private Integer linkType;
    /**
    * 企业微信STATE
    */
    private String state;
    /**
    * 客户经理二维码
    */
    private String qrCode;
    /**
    * 备注
    */
    private String remark;
    /**
    * 备注1
    */
    private String remark1;
    /**
    * 备注2
    */
    private String remark2;
    /**
    * 所属机构号
    */
    private String orgCode;
    /**
    * 欢迎语id
    */
    private String welcomeId;
}