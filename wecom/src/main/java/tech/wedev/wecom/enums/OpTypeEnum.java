package tech.wedev.wecom.enums;

public enum OpTypeEnum {
    READ_MSG(1,"阅读资讯"),
    ;

    private int code;
    private String desc;

    OpTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
