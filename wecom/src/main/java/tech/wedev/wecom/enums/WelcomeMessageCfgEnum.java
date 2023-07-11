package tech.wedev.wecom.enums;

public enum WelcomeMessageCfgEnum{
    ;
    public enum TypeENUM implements BaseStringEnum{
        H5("0","H5"),
        SHORT_VIDEO("1","短视频"),
        IMAGE_TEXT("2","图文"),
        APP("3","小程序"),
        ACTION("4","活动"),
        IMAGE("5","图片"),
        AUDIO("6","音频"),
        VIDEO("7","视频"),
        TEXT("8","仅文本"),
        ;

        private String code;
        private String desc;
        TypeENUM(String code, String desc) {
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

    public enum IsDefaultENUM implements BaseStringEnum{
        // 默认配置：1-默认配置
        NOT_DEFAULT("0","未默认"),
        IS_DEFAULT("1","默认")
        ;
        private String code;
        private String desc;
        IsDefaultENUM(String code, String desc) {
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

    public enum SourceENUM implements BaseStringEnum{
        //来源：1-内容库
        DEFAULT("0","未知来源"),
        ARTICLE("1","内容库")
        ;
        private String code;
        private String desc;
        SourceENUM(String code, String desc) {
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

}
