package tech.wedev.wecom.entity.common;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ExcelDatum {
    @ExcelProperty(value = "SAP单号", index = 0)
    @ColumnWidth(15)
    private String sapNo;

    //@ExcelProperty(value = "执行结果", index = 1)
    //@ColumnWidth(75)
    @ExcelIgnore
    private String result;

    @ExcelProperty(value = "执行时间", index = 1)
    @DateTimeFormat(value = "yyyy-MM-dd")
    @ColumnWidth(75)
    private Date createTime;
}
