package tech.wedev.wecom.entity.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
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
@KeySequence(value = "BASE_SEQUENCE")
public class BasicPO implements Serializable {
    /**
     * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 是否删除，0.存在，1.删除
     */
    @JSONField(serialize = false)
    //@TableLogic(value = "'0'", delval = "'1'")
    @TableLogic(value = "0", delval = "1")
    @TableField(value = "is_deleted", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private Boolean isDelete;

    @TableField(exist = false)
    private Integer isDeleted;

    /**
     * 创建人ID
     */
    @JSONField(serialize = false)
    @TableField(value = "create_id", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private Long createId;
    /**
     * 创建时间
     */
    @JSONField(serialize = false)
    @TableField(value = "gmt_create", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private Date gmtCreate;
    /**
     * 修改人ID
     */
    @JSONField(serialize = false)
    @TableField(value = "modified_id", fill = FieldFill.INSERT_UPDATE)
    private Long modifiedId;
    /**
     * 修改时间
     */
    @JSONField(serialize = false)
    @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
    /**
     * 机构代码
     */
//    private String orgCode;
    /**
     * 企业微信主体ID
     */
    @JSONField(serialize = false)
    @TableField(value = "corp_id", updateStrategy = FieldStrategy.NEVER)
    private String corpId;
}
