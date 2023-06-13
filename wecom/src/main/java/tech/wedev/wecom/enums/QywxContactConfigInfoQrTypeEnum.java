package tech.wedev.wecom.enums;

public enum QywxContactConfigInfoQrTypeEnum implements BaseStringEnum{
    CHANNEL_LIVE_CODE("1","渠道活码"),
    CUSTOM_CODE("2","一客一码"),
    REMARK_CODE("3","备注码")
    ;
    private final String code;
    private final String desc;

    QywxContactConfigInfoQrTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
