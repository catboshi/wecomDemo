package tech.wedev.wecom.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
public class RequestV1Private {
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Link {
        /**
         * 图文消息标题，不超过128个字符
         */
        @JSONField(name = "title")
        private String title;

        /**
         * 图文消息封面Url
         */
        @JSONField(name = "picurl")
        private String picurl;

        /**
         * 图文消息的描述，不超过512个字符
         */
        @JSONField(name = "desc")
        private String desc;

        /**
         * 图文消息的链接
         */
        @JSONField(name = "url")
        private String url;
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Text {
        /**
         *消息文本内容，不超过498日个字苷
         */
        @JSONField(name = "content")
        private String content;
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Miniprogram{
        /**
         * 小程序消息标趣，不超过64个字节
         */
        @JSONField (name = "title")
        private String title;

        /**
        * 小程序消息封面的mediaid
         */
        @JSONField (name = "pic_media_id")
        private String pic_media_id;

        /**
        * 小程序appid
        */
        @JSONField (name = "appid")
        private String appid;

        /**
         * 小程序page路径
         */
        @JSONField (name = "page")
        private String page;
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attachments {
        /**
         * 附件类型
         */
        @JSONField(name = "msgtype")
        private String msgtype;

        @JSONField(name = "Link")
        private Link link;

        @JSONField(name = "miniprogram")
        private Miniprogram miniprogram;
    }
}
