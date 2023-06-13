package tech.wedev.wecom.enums;

import java.util.HashMap;
import java.util.Map;

public enum AttachmentsMsgTypeEnum implements BaseStringEnum{
    H5("O","link") {
        Map<String, String> map = new HashMap<>();
        String value = "";
        @Override
        public void setValue(String ... args) {
            value = args[0];
        }

        @Override
        public String getValue() {
            return value;
        }

        /*@Override
        public Map<String, String> getMap() {
            map.put("title", "articleTitle");
            map.put("picurl", null);
            map.put("desc", "articleAbstract");
            map.put("url", "articleLink");
            return map;
        }*/
    },
    IMAGE_TEXT("2", "link") {
        Map<String, String> map = new HashMap<>();
        String value = "";
        @Override
        public void setValue(String ... args) {
            value = args[0];
        }

        @Override
        public String getValue() {
            return value;
        }

        /*@Override
        public Map<String, String> getMap() {
            map.put("title", "articleTitle");
            map.put("picurl", null);
            map.put("desc", "articleAbstract");
            map.put("url", "articleLink");
            return map;
        }*/
    },
    MINIPROGRAN("3", "miniprogram") {
        Map<String, String> map = new HashMap<>();
        String value = "";
        @Override
        public void setValue(String ... args) {
            value = args[1];
        }

        @Override
        public String getValue() {
            return value;
        }

        /*@Override
        public Map<String, String> getMap() {
            map.put("title", "articleTitle");
            map.put("pic_media_id", null);
            map.put("appid", "appid");
            map.put("page", "appLink");
            return map;
        }*/
    },
    TEXT("8", "text") {
        @Override
        public void setValue(String ... args) {
        }

        @Override
        public String getValue() {
            return "";
        }

        /*@Override
        public Map<String, String> getMap() {
            return null;
        }*/
    };

    private final String code;
    private final String desc;

    AttachmentsMsgTypeEnum (String code, String desc) {
        this.code=code;
        this.desc=desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public abstract void setValue(String... args);
    public abstract String getValue();
//    public abstract Map<String,String> getMap();
}
