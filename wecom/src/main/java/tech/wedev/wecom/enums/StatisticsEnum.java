package tech.wedev.wecom.enums;

public enum StatisticsEnum implements BaseIntegerEnum {
    MIN(1, "min"),
    MAX(2, "max"),
    COUNT(3, "count"),
    SUM(4, "sum"),
    AVG(5, "avg");
    private Integer code;
    private String desc;

    StatisticsEnum(Integer code, String desc) {
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
