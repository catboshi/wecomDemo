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
@TableName("op_log")
public class OpLogPO extends BasicPO{
    /**
     * 机构代码
     */
    private String orgCode;
    /**
     * 操作内容：存json格式
     */
    private String opContent;
    /**
     * 操作人userid
     */
    private String opUserId;
    /**
     * 操作人统一认证号
     */
    private String opTellerno;
    /**
     * 操作时间
     */
    private Date opTime;
    /**
     * 操作类型：1-阅读资讯，其他后续补充
     */
    private int opType;

}
