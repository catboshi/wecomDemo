package tech.wedev.wecom.enums;

public enum WechatEventCallBackChangeTypeEnum implements BaseStringEnum{
    add_external_contact("add", "addExternalContactTask"),
    add_half_external_contact("add_half", "addExternalContactTask"),
    edit_external_contact("edit", "editExternalContactTask"),
    del_external_contact("del", "delExternalContactTask"),
    ;
    private final String code;
    private final String desc;

    WechatEventCallBackChangeTypeEnum(String code, String desc) {
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
