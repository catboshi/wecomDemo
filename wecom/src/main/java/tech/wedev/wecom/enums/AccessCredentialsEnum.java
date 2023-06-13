package tech.wedev.wecom.enums;

public interface AccessCredentialsEnum {
    enum UrlParamType{
        APPLICATION("APPLICATION","自建应用"),
        COMMUNICATION("COMMUNICATION","通讯录"),
        EXTERNAL_CONTACT("EXTERNAL_CONTACT","外部联系人"),
        MSG_AUDIT("MSG_AUDIT","会话存档"),

        CORP_JSAPI_TICKET("CORP_JSAPI_TICKET", "企业主体调用JSJDK凭证"),
        AGENT_JSAPI_TICKET("AGENT_JSAPI_TICKET", "自建应用调用JSJDK凭证"),
        ;

        private final String code;
        private final String desc;

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        UrlParamType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }
}
