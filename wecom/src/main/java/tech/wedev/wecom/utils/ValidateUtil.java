package tech.wedev.wecom.utils;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * 校验工具类
 */
public final class ValidateUtil{
    private ValidateUtil() {
    }

    /**
     * 获取校验的工厂的信息
     */
    private static final javax.validation.Validator VALIDATOR = Validation.byProvider(HibernateValidator.class)
            .configure()
            //快速失败模式开启，当检测到有一项失败立即停止
            .failFast(true)
            .buildValidatorFactory()
            .getValidator();

    /**
     * 进行参数中的bean校验
     * @param obj 参数中bean类型参数
     * @param groups 分组信息
     */
    public static void validBeanParam(Object obj, Class<?> ... groups){
        Set<ConstraintViolation<Object>> validResult = VALIDATOR.validate(obj, groups);
        throwConstraintViolationException(validResult);
    }

    /**
     * 对于Hibernate 基本校验bean放在参数中的情况的校验【例如 User getUserInfoById(@NotNull(message = "不能为空") Integer id);】
     * @param obj 当前的实例
     * @param method 实例的方法
     * @param params 参数
     */
    public static void validMethodParams(Object obj, Method method, Object[] params){
        ExecutableValidator validatorParam = VALIDATOR.forExecutables();
        Set<ConstraintViolation<Object>> validResult = validatorParam.validateParameters(obj, method, params);
        throwConstraintViolationException(validResult);
    }

    /**
     * 判断校验的结果是否存在异常
     * @param validResult 校验结果
     */
    private static void throwConstraintViolationException(Set<ConstraintViolation<Object>> validResult) {
        if(validResult != null && !validResult.isEmpty()){
            throw new IllegalArgumentException(validResult.iterator().next().getMessage());
        }
    }
}
