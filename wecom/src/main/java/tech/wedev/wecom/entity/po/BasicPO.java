package tech.wedev.wecom.entity.po;

import tech.wedev.wecom.enums.BaseDeletedEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicPO implements Serializable {
    /**
     * 主键
    */
    private Long id;
    /**
     * 是否删除，0.存在，1.删除
     */
    private BaseDeletedEnum isDeleted;
    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 修改时间
     */
    private Date gmtModified;
    /**
     * 创建人ID
     */
    private Long createId;
    /**
     * 修改人ID
     */
    private Long modifiedId;
    /**
     * 机构代码
     */
//    private String orgCode;
    /**
     * 企业微信主体ID
     */
    private String corpId;
}
