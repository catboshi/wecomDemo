package tech.wedev.wecom.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @see BasicPO
 */
@Deprecated
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasePO implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 是否删除，0-存在；1-删除
     * @see tech.wedev.wecom.enums.BaseDeletedEnum
     */
    private Integer isDeleted;
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
    private Long orgId;

    private Long orgCode;

    /**
     * 企业微信主体ID
     */
    private String corpId;

}

