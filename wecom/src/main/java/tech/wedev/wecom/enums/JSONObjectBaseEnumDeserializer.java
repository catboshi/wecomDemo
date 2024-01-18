package tech.wedev.wecom.enums;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import tech.wedev.wecom.utils.EnumUtil;
import tech.wedev.wecom.utils.ReflectUtil;

import java.lang.reflect.Type;

public class JSONObjectBaseEnumDeserializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Object value = parser.parse();
        if (ReflectUtil.isSuperInterface((Class) type, BaseIntegerEnum.class)) {
            return (T) EnumUtil.getByIntCode((Class) type, (Integer) value);
        } else if (ReflectUtil.isSuperInterface((Class) type,BaseStringEnum.class)) {
            return (T) EnumUtil.getByStringCode((Class) type, (String) value);
        } else {
            return (T) value;
        }
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
