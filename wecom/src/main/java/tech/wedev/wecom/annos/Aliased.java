package tech.wedev.wecom.annos;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Aliased {
    /**
     * 对方字段别名
     */
    String value();
}
