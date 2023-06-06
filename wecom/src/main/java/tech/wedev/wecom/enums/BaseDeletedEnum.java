package tech.wedev.wecom.enums;

public enum BaseDeletedEnum implements BaseIntegerEnum {
    EXISTS(0, "存在"),
    DEL(1, "删除");
    private Integer code;
    private String desc;

    BaseDeletedEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
