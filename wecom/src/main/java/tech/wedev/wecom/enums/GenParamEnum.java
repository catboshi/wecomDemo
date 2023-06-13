package tech.wedev.wecom.enums;

public enum GenParamEnum {
    COMMUNICATION(GenParamTypeEnum.WECOM_API),
    EXTERNAL_CONTACT(GenParamTypeEnum.WECOM_API),
    MSG_AUDIT(GenParamTypeEnum.WECOM_API),
    APPLICATION(GenParamTypeEnum.WECOM_API),
    CORP_ID(GenParamTypeEnum.WECOM_API),
    //资讯上新通知开关
    NOTIFY_SWITCH(GenParamTypeEnum.WECOM_NOTIFY);

    private GenParamTypeEnum paramTypeEnum;

    GenParamEnum(GenParamTypeEnum paramTypeEnum){
        this.paramTypeEnum=paramTypeEnum;
    }

    public GenParamTypeEnum getParamTypeEnum(){
        return paramTypeEnum;
    }

    public String getName(){
        return this.name();
    }
    public enum GenParamTypeEnum{
        API_PROXY,
        URL_TRACK,
        URL_PREFIX,
        WECOM_OUTER_NOTIFY,
        EVENT_SWITCH,
        WELCOME_MSGTYPE_VALUE,
        WELCOME_MSGTYPE_CODE,
        WECOM_API,
        CHAT_SAVE,
        WECOM_INNER_NOTIFY,
        GRAY_API,
        CUSTOM_TRIGGER_EXPORT,
        STATISTICS_EXPORT,
        WECOM_NOTIFY;
        public String getName(){
            return this.name();
        }
    }
}
