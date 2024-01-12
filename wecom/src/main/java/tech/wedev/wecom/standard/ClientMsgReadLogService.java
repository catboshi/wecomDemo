package tech.wedev.wecom.standard;

import tech.wedev.wecom.entity.vo.ResponseVO;

import javax.servlet.http.HttpServletRequest;
import java.io.OutputStream;
import java.util.Map;

public interface ClientMsgReadLogService {

    /**
     * 处理参数并保存入库
     * @param articleSource
     * @param code
     * @return ResponseVO
     */
    ResponseVO saveLog(String articleSource, String code);

    void export(HttpServletRequest request, OutputStream out, Map<String, Object> param);
}
