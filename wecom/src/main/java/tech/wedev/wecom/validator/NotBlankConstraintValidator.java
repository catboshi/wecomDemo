package tech.wedev.wecom.validator;

import tech.wedev.wecom.annos.NotBlank;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankConstraintValidator implements ConstraintValidator<NotBlank, String> {
    @Override
    public void initialize(NotBlank annotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(StringUtils.isBlank(value)){
            return false;
        }
        return true;
    }
}
