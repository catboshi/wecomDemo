package tech.wedev.wecom.validator;

import cn.hutool.core.util.NumberUtil;
import tech.wedev.wecom.annos.RequiredNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义必须为数字校验
 */
public class NumberValidator implements ConstraintValidator<RequiredNumber, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return NumberUtil.isNumber(String.valueOf(value));
    }
}
