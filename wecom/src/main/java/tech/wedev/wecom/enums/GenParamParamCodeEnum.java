package tech.wedev.wecom.enums;

public enum GenParamParamCodeEnum implements BaseStringEnum {
    OUTER_TOKEN("OUTER_TOKEN", "OUTER_TOKEN"),
    OUTER_ENCODING_AESKEY("OUTER_ENCODING_AESKEY", "OUTER_ENCODING_AESKEY"),
    ;

    private final String code;
    private final String desc;

    GenParamParamCodeEnum(String code, String desc) {
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
