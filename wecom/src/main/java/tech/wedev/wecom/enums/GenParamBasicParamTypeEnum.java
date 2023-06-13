package tech.wedev.wecom.enums;

public enum GenParamBasicParamTypeEnum implements BaseStringEnum {
    WECOM_API("WECOM_API", "WECOM_API"),
    WECOM_OUTER_NOTIFY("WECOM_OUTER_NOTIFY", "WECOM_OUTER_NOTIFY"),
    ;
    private final String code;
    private final String desc;

    GenParamBasicParamTypeEnum(String code, String desc) {
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