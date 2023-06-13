package tech.wedev.wecom.enums;

public interface APIErrorMsgEnum {
    enum NOS{
        GET_INSTANCE("5565550404", "获取NOS连接异常"),
        COMMUNICATION("5565550501", "从NOS获取Token异常，通讯录接口"),
        EXTERNAL_CONTACT("5565550502", "从NOS获取Token异常，外部联系人接口"),
        MSG_AUDIT("5565550503", "从NOS获取Token异常，会话存档接口"),
        APPLICATION("5565550504", "从NOS获取Token异常，自建应用接口"),
        ;

        private final String code;
        private final String desc;


        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }


        NOS(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }
}
