package tech.wedev.wecom.annos;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface StopWatch {
//    避免在方法内部调用，否则会失效
}
