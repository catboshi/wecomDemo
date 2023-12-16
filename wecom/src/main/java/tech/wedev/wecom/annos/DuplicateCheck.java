package tech.wedev.wecom.annos;

import tech.wedev.wecom.validator.DuplicateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义校验注解
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = {DuplicateValidator.class})
public @interface DuplicateCheck {

    /**
     * 表名
     */
    String tableName();

    /**
     * 表字段
     */
    String field();

    /**
     * 实体类属性
     */
    String property();

    /**
     * 默认错误提示消息
     */
    String message() default "";

    // 分组
    Class<?>[] groups() default {};

    // 负载
    Class<? extends Payload>[] payload() default {};

}

