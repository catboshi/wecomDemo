package tech.wedev.wecom.entity.qo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ClientShareUploadQO {
    /**
     * 接入应用
     */
    private String articleApp;
    /**
     * 资源编号
     */
    private String articleSource;
    /**
     * 内容编号
     */
    private String contentId;
    /**
     * 标题
     */
    private String articleTitle;
    /**
     * 内容类型（后缀名）
     */
    private String suffix;
    /**
     * 租户ID
     */
    private String corpId;
}
