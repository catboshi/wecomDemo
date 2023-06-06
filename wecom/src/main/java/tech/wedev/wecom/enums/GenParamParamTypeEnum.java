package tech.wedev.wecom.enums;

public enum GenParamParamTypeEnum implements BaseStringEnum{
    WECOM_OUTER_NOTIFY("WECOM_OUTER_NOTIFY", "WECOM_OUTER_NOTIFY"),
    ;

    private final String code;
    private final String desc;
    GenParamParamTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getDesc() {
        return null;
    }
}
