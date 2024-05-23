package tech.wedev.wecom.entity.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class WecomImportDataExcelEntity {

    @ExcelProperty(value = "产品型号")
    private String invType;

    @ExcelProperty(value = "产品名称")
    private String invName;

    @ExcelProperty(value = "产品代码（物料编码）")
    private String invCode;

    @ExcelProperty(value = "零件图号")
    private String partNo;

    @ExcelProperty(value = "NO")
    private String no;

    @ExcelProperty(value = "关键特性")
    private String keyFeatures;

    @ExcelProperty(value = "规格值")
    private String specificationValue;

    @ExcelProperty(value = "指标上限")
    private String quotaCap;

    @ExcelProperty(value = "指标下限")
    private String quotaFloor;

    @ExcelProperty(value = "指标标准")
    private String quotaCanon;

    @ExcelProperty(value = "测量工具")
    private String measureTool;

    @ExcelProperty(value = "抽检方案")
    private String samplingPlan;

    @ExcelProperty(value = "检验频次")
    private String inspectionFrequency;
}
