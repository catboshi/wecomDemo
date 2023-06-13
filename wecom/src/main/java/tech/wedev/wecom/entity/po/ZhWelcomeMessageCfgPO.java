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
@TableName("zh_welcome_message_cfg")
public class ZhWelcomeMessageCfgPO extends BasicPO {
    /**
    * 总行营销推文表ID
    */
    private Long articleId;
    /**
    * 来源：1-内容库
    */
    private String source;
    /**
    * 统一认证号
    */
    private String tellerno;
    /**
    * 姓名
    */
    private String tellername;
    /**
    * 机构号
    */
    private String orgCode;
    /**
    * 机构名称
    */
    private String orgName;
    /**
    * 类型：0-H5，1-短视频，2-图文，3-小程序，4-活动，5-图片，6-音频，7-视频，8-仅文本
    */
    private String type;
    /**
    * 类型名称
    */
    private Integer typeName;
    /**
    * 欢迎语名称
    */
    private String welcomeName;
    /**
    * 欢迎语内容
    */
    private String welcomeContent;
    /**
    * 欢迎语文字
    */
    private String welcomeWord;
    /**
    * 默认配置：1是默认配置
    */
    private String isDefault;
}