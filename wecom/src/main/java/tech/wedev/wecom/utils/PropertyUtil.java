package tech.wedev.wecom.utils;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.MissingResourceException;

/**
 * <p>对接配置中心</p>
 * @version 1.0
 */
public class PropertyUtil implements EnvironmentAware {

    private static Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        PropertyUtil.environment = environment;
    }

    public static String getValue(String key) {
        String property = environment.getProperty(key);
        if (property == null) {
            throw new MissingResourceException("属性不存在", "", key);
        }
        return property;
    }

    public static String getValueByStartWith(String startWith) {
        return environment.getProperty(startWith);
    }
}
