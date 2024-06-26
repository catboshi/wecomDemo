package tech.wedev.wecom.enums;

public enum JwtKeyEnum {
    CORP_ID("CORP_ID","租户ID"),
    ;

    private String code;
    private String value;

    JwtKeyEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
