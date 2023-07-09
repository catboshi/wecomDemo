package tech.wedev.wecom.exception;

public enum ExceptionCode implements ErrorCode{
    /**
     * 基础返回码枚举
     */
    SUCCESS(200,"成功"),
    INVALID_PARAMETER(403, "无效的参数"),
    ERROR_500(500,"系统异常"),

    /**
     * 逻辑异常以5开头，如数据库记录大于1条，插入数据库的值不该为空的值为空等
     */
    MORE_THAN_ONE(501,"大于一条记录"),
    LESS_THAN_ONE(502,"小于一条记录"),
    FORBIDDEN_QYWX(505, "请先去禁用企微账号"),

    //发送新客户欢迎语-异常定义
    LACK_WELCOMECODE(61049, "缺少欢迎语code参数"),
    CUSTMGR_ACCOUNT_ERROR(61050,"所查人员账号不存在或状态异常"),
    NOT_EXIST_DEFAULT_WECOMEMESSAGE(61051, "默认欢迎语配置信息不存在"),
    UNSUPPORT_MSGTYPE(61052, "不支持的附件类型，仅H5、图文、小程序"),


    /**
     * 企微API封装异常
     */
    REDIS_CONNECT_ERROR(404,"REDIS连接异常"),
    PARAMETER_SECRET_ERROR(403,"SECRET参数配置异常"),
    PARAMETER_CORP_ID_ERROR(403,"CORP_ID参数配置异常"),
    REQUEST_ERROR(500,"请求企业微信API异常"),
    ;


    private final Integer code;
    private final String msg;
    ExceptionCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
