package tech.wedev.wecom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.wedev.wecom.standard.IWecomDataImportService;


@RestController
@RequestMapping("/import")
@RequiredArgsConstructor
public class WecomDataBulkImportController {

    private final IWecomDataImportService wecomDataImportService;

    @PostMapping("/parseExcel")
    public Object bulkImportExcel(@RequestParam("file") MultipartFile file) {
        return wecomDataImportService.bulkImportExcel(file);
    }

}
