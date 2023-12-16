package tech.wedev.wecom.annos;

import tech.wedev.wecom.validator.NumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = NumberValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, TYPE, ANNOTATION_TYPE, CONSTRUCTOR, FIELD, PARAMETER})
public @interface RequiredNumber {

    String message() default "字段格式错误，必须为整数";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
