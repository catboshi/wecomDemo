package tech.wedev.wecom.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ExcelDatum {
    @ExcelProperty(value = "SAP单号")
    @ColumnWidth(15)
    private String sapNo;

    @ExcelProperty(value = "执行结果")
    @ColumnWidth(75)
    private String result;
}
