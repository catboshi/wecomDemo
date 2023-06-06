package tech.wedev.wecom.annos;


import tech.wedev.wecom.validator.NotBlankConstraintValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankConstraintValidator.class)
public @interface NotBlank {
    boolean required() default true;

    String message() default "参数值为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
