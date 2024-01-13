package tech.wedev.wecom.entity.po;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import tech.wedev.wecom.annos.DuplicateCheck;
import tech.wedev.wecom.annos.NotWhere;
import tech.wedev.wecom.tools.ValidatorGroup;

import java.util.Date;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("zh_corp_info")
@Accessors(chain = true)
@DuplicateCheck(tableName = "zh_corp_info", field = "corp_id",
        property = "corpId", message = "租户主体ID不能重复", groups = ValidatorGroup.Insert.class)
@ApiOperation(value = "租户信息")
public class CorpInfo extends BasicPO {
    /**
     * 自建应用agentId
     */
    @ExcelProperty(value = "自建应用agentId", index = 0)
    @ColumnWidth(15)
    private String agentApplication;
    /**
     * 自建应用调用JSJDK凭证
     */
    private String agentJsapiTicket;
    /**
     * 企业主体调用JSJDK凭证
     */
    private String corpJsapiTicket;
    /**
     * 企微SECRET（自建应用）
     */
    @JSONField(serialize = false)
    @TableField(value = "secret_application", fill = FieldFill.INSERT_UPDATE)
    private String secretApplication;
    /**
     * 企微SECRET（通讯录）
     */
    private String secretCommunication;
    /**
     * 企微SECRET（外部联系人）
     */
    private String secretExternalContact;
    /**
     * 企微SECRET（会话存档）
     */
    private String secretMsgAudit;
    /**
     * TOKEN（自建应用）
     */
    private String tokenApplication;
    /**
     * TOKEN（通讯录）
     */
    private String tokenCommunication;
    /**
     * TOKEN（外部联系人）
     */
    private String tokenExternalContact;
    /**
     * TOKEN（会话存档）
     */
    @ExcelIgnore
    private String tokenMsgAudit;
    /**
     * TOKEN（自建应用）修改时间
     */
    @ApiModelProperty(name = "TOKEN（自建应用）修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("TokenApplicationModified")
    private Date tokenApplicationModified;
    /**
     * TOKEN（通讯录）修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "TOKEN（通讯录）修改时间")
    private Date tokenCommunicationModified;
    /**
     * TOKEN（外部联系人）修改时间
     */
    private Date tokenExternalContactModified;
    /**
     * TOKEN（会话存档）修改时间
     */
    private Date tokenMsgAuditModified;
    /**
     * 自建应用调用JSJDK凭证修改时间
     */
    private Date agentJsapiTicketModified;
    /**
     * 企业主体调用JSJDK凭证修改时间
     */
    private Date corpJsapiTicketModified;
    /**
     * 会话存档存取私钥
     */
    private String chatSavePriKey;

    @NotWhere
    //指定此字段不查询
    @TableField(exist = false)
    private Integer currentPage = 1;

    @NotWhere
    //指定此字段不查询
    @TableField(exist = false)
    private Integer pageSize = 10;
}