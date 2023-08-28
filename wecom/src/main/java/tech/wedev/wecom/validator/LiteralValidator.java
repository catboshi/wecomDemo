package tech.wedev.wecom.validator;

import tech.wedev.wecom.annos.Validator;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义校验注解处理类
 */
public class LiteralValidator implements ConstraintValidator<Validator, Object> {

    private String[] strValues;
    private int[] intValues;

    @Override
    public void initialize(Validator constraintAnnotation) {
        strValues = constraintAnnotation.strValues();
        intValues = constraintAnnotation.intValues();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof String) {
            for (String s : strValues) {
                if (s.equals(value)) {
                    return true;
                }
            }
        } else if (value instanceof Integer) {
            for (int s : intValues) {
                if (s == (Integer) value) {
                    return true;
                }
            }
        }
        return false;
    }
}
