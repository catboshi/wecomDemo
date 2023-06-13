package tech.wedev.wecom.enums;

public enum GenParamBasicParamCodeEnum implements BaseStringEnum {
    APPLICATION("APPLICATION","APPLICATION"),
    COMMUNICATION("COMMUNICATION", "COMMUNICATION"),
    EXTERNAL_CONTACT("EXTERNAL_CONTACT", "EXTERNAL_CONTACT"),
    MSG_AUDIT("MSG_AUDIT", "MSG_AUDIT"),
    OUTER_TOKEN("OUTER_TOKEN", "OUTER_TOKEN"),
    OUTER_ENCODING_AESKEY("OUTER_ENCODING_AESKEY", "OUTER_ENCODING_AESKEY"),
    ;

    private final String code;
    private final String desc;

    GenParamBasicParamCodeEnum(String code, String desc) {
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
