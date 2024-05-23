package tech.wedev.wecom.standard;

import org.springframework.web.multipart.MultipartFile;
import tech.wedev.wecom.entity.vo.CommonResult;

public interface IWecomDataImportService {
    CommonResult<Void> bulkImportExcel(MultipartFile file);

}
