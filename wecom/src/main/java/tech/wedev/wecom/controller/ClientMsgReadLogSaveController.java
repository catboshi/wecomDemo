package tech.wedev.wecom.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.wedev.wecom.entity.vo.ResponseVO;
import tech.wedev.wecom.standard.ClientMsgReadLogService;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController("/client/msg")
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
}
