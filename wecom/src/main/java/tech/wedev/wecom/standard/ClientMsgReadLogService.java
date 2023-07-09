package tech.wedev.wecom.standard;

import tech.wedev.wecom.entity.vo.ResponseVO;

public interface ClientMsgReadLogService {

    /**
     * 处理参数并保存入库
     * @param articleSource
     * @param code
     * @return ResponseVO
     */
    ResponseVO saveLog(String articleSource, String code);
}
