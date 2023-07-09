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
@TableName("zh_wecom_market_article")
public class WecomMarketArticleQO extends BasicQO {
    /**
    * 接入应用
    */
    private String articleApp;
    /**
    * 可见范围
    */
    private String articleVisible;
    /**
    * 资源编号
    */
    private String articleSource;
    /**
    * 图文url地址
    */
    private String articleLink;
    /**
    * 标题
    */
    private String articleTitle;
    /**
    * 图文消息的摘要
    */
    private String articleAbstract;
    /**
    * 发布时间
    */
    private Date releaseTime;
    /**
    * 推文上送创建时间
    */
    private Date articleCreateTime;
    /**
    * 系统创建时间
    */
    private Date createTime;
    /**
    * 变更时间
    */
    private Date modifyTime;
    /**
    * 缩略图url
    */
    private String articleThumbnail;
    /**
    * 资源格式编码
    */
    private String sourceFormat;
    /**
    * 资源格式名称
    */
    private String sourceFormatName;
    /**
    * 展现业务机构编码
    */
    private String businessCode;
    /**
    * 展现业务机构名称
    */
    private String businessName;
    /**
    * 资源类型编码
    */
    private String sourceType;
    /**
    * 资源类型名称
    */
    private String sourceTypeName;
    /**
    * 是否有效标志；0有效，1无效
    */
    private Integer isDelete;
    /**
    * 申请人
    */
    private String applyMgrId;
    /**
    * 申请人所属机构
    */
    private String applyCorpId;
    /**
    * 开始时间
    */
    private Date startTime;
    /**
    * 截止时间
    */
    private Date endTime;
    /**
    * 推文发布状态
    */
    private Integer status;
    /**
    * orgin_url唯一标识，md5生成
    */
    private String orginUrlId;
    /**
    * 是否置顶：0-否，1-是
    */
    private Integer isTop;
    /**
    * 是否轮播：0-否，1-是
    */
    private Integer isCarousel;
    /**
    * 发布人姓名
    */
    private String applicantName;
    /**
    * 地区号
    */
    private String areacode;
    /**
    * 内容编号
    */
    private String contentId;
    /**
    * 格式（用于存放contentId的格式）
    */
    private String suffix;
    /**
    * 语言
    */
    private String langcode;
    /**
    * 缩略图
    */
    private String thumbnail;
    /**
    * 缩略图格式
    */
    private String thumbnailFormat;
    /**
    * 视频号ID
    */
    private String videoId;
    /**
    * 视频链接
    */
    private String videoLink;
    /**
    * 视频号名称
    */
    private String videoName;
    /**
    * 小程序ID
    */
    private String appid;
    /**
    * 小程序跳转路径
    */
    private String appLink;
    /**
    * 媒体文件ID
    */
    private String mediaId;
    /**
    * 临时素材上传时间
    */
    private Date mediaCreatedTime;
}