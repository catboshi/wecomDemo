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
@TableName("zh_corp_info")
public class CorpInfoQO extends BasicQO {
    /**
    * 自建应用agentId
    */
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
    private String tokenMsgAudit;
    /**
    * TOKEN（自建应用）修改时间
    */
    private Date tokenApplicationModified;
    /**
    * TOKEN（通讯录）修改时间
    */
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
}