package tech.wedev.wecom.entity.qo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @see BasicQO
 */
@Deprecated
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseQO implements Serializable {
    /**
     * 页大小
     */
    private Integer pageSize;
    /**
     * 页号
     */
    private Integer pageNum;
    /**
     * 主键
     */
    private Long id;
    /**
     * id集合
     */
    private List<Long> ids;
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
     * 创建时间起始时间
     */
    private Date gmtCreateStart;

    /**
     * 创建时间结束时间
     */
    private Date gmtCreateEnd;

    private Boolean isAsc;

    private Long startId;

    private Date gmtModifiedStart;
    private Date gmtModifiedEnd;

    private Long orgId;

    private Long orgCode;

    /**
     * 企业微信主体ID
     */
    private String corpId;

}
