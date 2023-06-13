package tech.wedev.wecom.entity.qo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import tech.wedev.wecom.annos.NotWhere;
import tech.wedev.wecom.entity.po.BasicPO;
import tech.wedev.wecom.enums.BaseDeletedEnum;
import tech.wedev.wecom.enums.StatisticsEnum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicQO implements Serializable {
    /**
     * 页大小
     */
    @NotWhere
    private Integer pageSize;

    /**
     * 页号
     */
    @NotWhere
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
     * 创建时间起始时间
     */
    private Date gmtCreateStart;

    /**
     * 创建时间结束时间
     */
    private Date gmtCreateEnd;

    /**
     * 机构代码
     */
//    private String orgCode;

    /**
     * 企业微信主体ID
     */
    private String corpId;

    @NotWhere
    private Long startId;

    @NotWhere
    private StatisticsEnum statistics;

    @NotWhere
    private String statisticsField;

    @NotWhere
    private BasicPO updateValPO = new BasicPO();

    @NotWhere
    private List<String> fields;

    @NotWhere
    private Boolean isExcludeFields;

    @NotWhere
    private List<String> orderBys;

    @NotWhere
    private List<String> groupBys;

    /**
     * 字段顺序
     */
    @NotWhere
    private List<String> fieldOrders;

    @NotWhere
    private Integer total;

}
