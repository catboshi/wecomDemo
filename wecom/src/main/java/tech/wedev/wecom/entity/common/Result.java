package tech.wedev.wecom.entity.common;

import lombok.Data;

@Data
public class Result {
    //错误码
    private String code;
    //提示信息
    private String msg;
    //具体的内容
    private Object data;
    //功能总数
    private int pageCount;

}
