package tech.wedev.wecom.enums;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import tech.wedev.wecom.utils.EnumUtils;
import tech.wedev.wecom.utils.ReflectUtils;

import java.lang.reflect.Type;

public class JSONObjectBaseEnumDeserializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Object value = parser.parse();
        if (ReflectUtils.isSuperInterface((Class) type, BaseIntegerEnum.class)) {
            return (T) EnumUtils.getByIntCode((Class) type, (Integer) value);
        } else if (ReflectUtils.isSuperInterface((Class) type,BaseStringEnum.class)) {
            return (T) EnumUtils.getByStringCode((Class) type, (String) value);
        } else {
            return (T) value;
        }
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
