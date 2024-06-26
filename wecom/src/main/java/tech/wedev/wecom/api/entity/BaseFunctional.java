package tech.wedev.wecom.api.entity;

import lombok.var;
import tech.wedev.wecom.entity.po.WecomMarketArticlePO;
import tech.wedev.wecom.enums.AttachmentsMsgTypeEnum;
import tech.wedev.wecom.exception.ExceptionCode;
import tech.wedev.wecom.exception.WecomException;
import tech.wedev.wecom.request.RequestV1Private;
import tech.wedev.wecom.utils.StringUtil;

import java.util.Arrays;

//自定义函数&反射封装 <T> mapping <R>
public class BaseFunctional<T> {

    public T data;

    public BaseFunctional (T data) {
        this.data = data;
    }

/*    @Data
    public static class Object {
        RequestV1Private.Attachments attachments;
        public Object (RequestV1Private.Attachments attachments){
            this.attachments = attachments;
        }
    }*/

//    public static class Transforms{
//        public static Try<Object> transform (Class clazzT, Object ... objects) throws Throwable {
        public static Object transform (Object ... objects) {
            var msgType = (String) Arrays.stream(objects).filter(o -> o.getClass() == String.class).filter(o-> StringUtil.isNotBlank(String.valueOf(o))).findFirst().orElseThrow(()->new WecomException(ExceptionCode.INVALID_PARAMETER));
            var article = (WecomMarketArticlePO) Arrays.stream(objects).filter(o -> o.getClass() == WecomMarketArticlePO.class).findFirst().orElseThrow(()->new WecomException(ExceptionCode.INVALID_PARAMETER));
            var enum0 = Arrays.stream(AttachmentsMsgTypeEnum.values()).filter(a -> a.getDesc().equals(msgType)).findFirst().orElseThrow(() -> new WecomException(ExceptionCode.UNSUPPORT_MSGTYPE));
            switch (msgType){
                case "link":
//                    return new Object (RequestV1Private.Attachments.builder()
                    return RequestV1Private.Attachments.builder()
                            .msgtype(msgType)
                            .link(RequestV1Private.Link.builder()
                                    .title(article.getArticleTitle())
                                    .picurl(enum0.getValue())
                                    .desc(article.getArticleAbstract())
                                    .url(article.getArticleLink())
                                    .build())
                            .build();
                case "miniprogram":
//                    return new Object (RequestV1Private.Attachments.builder()
                    return RequestV1Private.Attachments.builder()
                            .msgtype(msgType)
                            .miniprogram(RequestV1Private.Miniprogram.builder()
                                    .title(article.getArticleTitle())
                                    .pic_media_id(enum0.getValue())
                                    .appid(article.getAppid())
                                    .page(article.getAppLink())
                                    .build())
                            .build();
                case "video":
                //...
                case "image":
                //...
                default:
//                    return new Object(null);
                    return null;
            }
            /*var msgType = (String) Arrays.stream(objects).filter(o -> o.getClass() == String.class).findFirst().orElse("");
            var article = (WecomMarketArticlePO) Arrays.stream(objects).filter(o -> o.getClass() == WecomMarketArticlePO.class).findFirst().orElse(null);
            var clazzR = Class.forName(clazzT.getName());
            var clazzA = Class.forName(clazzT.getName() + "$" + "Attachments");
            var clazzV = Class.forName(clazzT.getName() + "$" + StringUtils.capitalizeFirstLetter(msgType));
            var obj0 = ObjectUtils.strObjToType(clazzV.newInstance(), clazzV);
            var declaredFields1 = clazzV.getDeclaredFields();
            for (Field field : declaredFields1) {
                field.setAccessible(true);
                var enum0 = Arrays.stream(AttachmentsMsgTypeEnum.values()).filter(a -> a.getDesc().equals(msgType)).findFirst().orElse(null);
                if (!Optional.ofNullable(enum0.getMap().get(field.getName())).isPresent()) {field.set(obj0, enum0.getValue());continue;}
                Field field1 = article.getClass().getDeclaredField(enum0.getMap().get(field.getName()));
                field1.setAccessible(true);
                field.set(obj0,field1.get(article));
            }
            return Arrays.stream(clazzR.getDeclaredClasses())
                    .filter(a -> a == clazzA)
                    .findFirst()
                    .map(b -> Try.ofFailable(() -> {
                        var f0 = b.getDeclaredField("msgtype");
                        f0.setAccessible(true);
                        var f1 = b.getDeclaredField(msgType);
                        f1.setAccessible(true);
                        var instance = b.newInstance();
                        f0.set(ObjectUtils.strObjToType(instance, clazzA), msgType);
                        f1.set(ObjectUtils.strObjToType(instance, clazzA), obj0);
                        return instance;
                    }))
                    .orElse(null);*/
        }
//    }

    @FunctionalInterface
    public interface FN<V, R> {
//        Try<R> apply(V v);
        R apply(V v);
    }

//    public <R> BaseFunctional<Try<?>> map(FN<T, R> fn) {
    public <R> BaseFunctional<?> map(FN<T, R> fn) {
        return new BaseFunctional<>(fn.apply(this.data));
    }
}


