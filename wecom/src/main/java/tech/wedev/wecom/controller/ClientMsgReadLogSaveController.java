package tech.wedev.wecom.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.wedev.wecom.constants.CommonConstants;
import tech.wedev.wecom.entity.vo.ResponseVO;
import tech.wedev.wecom.standard.ClientMsgReadLogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/client/msg")
public class ClientMsgReadLogSaveController {

    @Autowired
    private ClientMsgReadLogService clientMsgReadLogService;

    /**
     * 保存日志并跳转
     */
    @RequestMapping(value = "/saveMsgReadLog/V1")
    public String saveMsgReadLog(String code, String articleSource, HttpServletResponse response) throws Exception {
        log.info("articleSource==" + articleSource);
        log.info("code==" + code);
        ResponseVO responseVO = clientMsgReadLogService.saveLog(articleSource, code);
        log.info(responseVO.toString());
        int retCode = responseVO.getRetCode();
        if (retCode == 200) {
            log.info("success------" + responseVO.getRetMsg());
            response.sendRedirect(responseVO.getRetMsg());
            return "";
        } else {
            log.info("error------" + responseVO.getRetMsg());
            return responseVO.getRetMsg();
        }
    }

    @PostMapping("/export")
    public void export(@RequestBody Map<String, Object> param, HttpServletRequest request, HttpServletResponse response) {
        String fileName = "导出日志结果";

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf8");
        try {
            response.setHeader(CommonConstants.CONTENT_DIS, CommonConstants.FILE_TITLE + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()) + CommonConstants.EXCEL_SUFFIX);
        } catch (UnsupportedEncodingException e) {
            response.setHeader(CommonConstants.CONTENT_DIS, CommonConstants.FILE_TITLE + fileName + CommonConstants.EXCEL_SUFFIX);
        }
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Cache-Control", "max-age=0");
        try(OutputStream out = response.getOutputStream()) {
            clientMsgReadLogService.export(request, out, param);
        } catch (IOException e) {
            log.error("下载导出日志结果异常：", e);
        }
    }
}
