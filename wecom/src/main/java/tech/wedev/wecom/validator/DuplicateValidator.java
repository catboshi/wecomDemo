package tech.wedev.wecom.validator;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import tech.wedev.wecom.annos.DuplicateCheck;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class DuplicateValidator implements ConstraintValidator<DuplicateCheck, Object>, ApplicationContextAware {
    private DuplicateCheck duplicateCheck;

    static ApplicationContext context;

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        JSONObject jsonObject = JSONUtil.parseObj(object);
        String table = duplicateCheck.tableName();
        String field = duplicateCheck.field();
        String property = duplicateCheck.property();
        Object propertyVal = jsonObject.get(property);
        //判断表中是否已有重复的记录
        String sql = StrUtil.format("select count(1) as duplicate from {} where {} = '{}'", table, field, propertyVal);
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        Integer duplicate = jdbcTemplate.queryForObject(sql, Integer.class);
        if (Objects.nonNull(duplicate) && duplicate != 0) {
            return false;
        }
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public void initialize(DuplicateCheck duplicateCheck) {
        this.duplicateCheck = duplicateCheck;
    }
}
