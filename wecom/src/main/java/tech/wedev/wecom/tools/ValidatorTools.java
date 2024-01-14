package tech.wedev.wecom.tools;

import org.hibernate.validator.HibernateValidator;
import org.springframework.stereotype.Component;
import tech.wedev.wecom.entity.vo.ResponseVO;
import tech.wedev.wecom.utils.StringUtil;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Component
public class ValidatorTools {
    private Validator validator;

    @PostConstruct
    public void init() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure().buildValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    public ResponseVO isValid(Object param, Class<?>... groups) {
        Set<ConstraintViolation<Object>> violations = validator.validate(param, groups);
        //集合为空说明参数均校验通过
        if (violations.isEmpty()) {
            return ResponseVO.success();
        }
        StringBuilder sb = new StringBuilder();
        //拼接参数校验错误信息
        for (ConstraintViolation violation : violations) {
            sb.append(violation.getMessage()).append(",");
        }
        return StringUtil.endsWith(sb.toString(), ",") ? ResponseVO.error(sb.substring(0, sb.length() - 1)) : ResponseVO.error(sb.toString());
    }
}
