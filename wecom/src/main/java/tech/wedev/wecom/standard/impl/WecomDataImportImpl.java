package tech.wedev.wecom.standard.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.read.listener.ReadListener;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.wedev.wecom.entity.bo.Int;
import tech.wedev.wecom.entity.bo.WecomImportDataExcelEntity;
import tech.wedev.wecom.entity.vo.CommonResult;
import tech.wedev.wecom.standard.IWecomDataImportService;

import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WecomDataImportImpl implements IWecomDataImportService {

    //private final WecomCheckDataImportMapper wecomCheckDataImportMapper;

    @Override
    public CommonResult<Void> bulkImportExcel(MultipartFile file) {
        if (file == null) {
            return CommonResult.error("请上传文件");
        }

        List<WecomImportDataExcelEntity> returnList = Lists.newArrayList();
        final List<String> errorList = Lists.newArrayList();
        try (InputStream in = file.getInputStream()) {
            Int rowNum = new Int(1);
            EasyExcelFactory.read(in).head(WecomImportDataExcelEntity.class).sheet(0).registerReadListener(new ReadListener<WecomImportDataExcelEntity>() {
                @SneakyThrows
                @Override
                public void invoke(WecomImportDataExcelEntity mesCheckDataExcelEntity, AnalysisContext analysisContext) {
                    if (mesCheckDataExcelEntity == null) {
                        return;
                    }
                    rowNum.incr();
                    check(mesCheckDataExcelEntity, rowNum, errorList);
                    if (CollUtil.isNotEmpty(errorList)) {
                        throw new ExcelAnalysisException(String.join(StrUtil.HTML_NBSP, errorList));
                    }
                    returnList.add(mesCheckDataExcelEntity);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    final int batchSize = 100;
                    log.info("所有数据解析完成，开始入库");
                    List<List<WecomImportDataExcelEntity>> partition = Lists.partition(returnList, batchSize);
                    for (List<WecomImportDataExcelEntity> singleList : partition) {
                        //List<WecomCheckDataMapperEntity> importList = BeanUtil.copyToList(singleList, WecomCheckDataMapperEntity.class);
                        //wecomCheckDataImportMapper.insertMultipleNoId(importList);
                    }
                    log.info("入库完成，总共导入{}条数据", returnList.size());
                }

                @Override
                public void onException(Exception exception, AnalysisContext context) throws Exception {
                    if (exception instanceof ExcelDataConvertException) {
                        ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
                        log.error("第{}行，第{}列解析异常", excelDataConvertException.getRowIndex() + 1,
                                excelDataConvertException.getColumnIndex() + 1);
                    } else {
                        log.error("解析异常：{}", exception.getMessage(), exception);
                        throw exception;
                    }
                }
            }).autoTrim(Boolean.FALSE).doRead();

        } catch (Exception e) {
            return CommonResult.message("导入失败: " + e.getMessage());
        }
        return CommonResult.message("导入成功");
    }

    private void check(WecomImportDataExcelEntity mesCheckDataExcelEntity, Int rowNum, List<String> errorList) {
        if (StringUtils.isBlank(mesCheckDataExcelEntity.getInvCode())) {
            errorList.add("第" + rowNum.getValue() + "行: 产品代码（物料编码）不能为空");
        }
    }

}
