package tech.wedev.wecom.validator;

import cn.hutool.core.util.NumberUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义必须为数字校验
 */
public class NumberValidator implements ConstraintValidator<RequireNumber, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return NumberUtil.isNumber(String.valueOf(value));
    }
}
