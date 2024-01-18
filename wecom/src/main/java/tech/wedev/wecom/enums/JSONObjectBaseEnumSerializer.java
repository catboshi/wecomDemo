package tech.wedev.wecom.enums;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import tech.wedev.wecom.utils.ReflectUtil;

import java.io.IOException;
import java.lang.reflect.Type;

public class JSONObjectBaseEnumSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (ReflectUtil.isSuperInterface((Class) fieldType, BaseIntegerEnum.class)) {
            serializer.write(((BaseIntegerEnum) object).getCode());
        } else if (ReflectUtil.isSuperInterface((Class) fieldType, BaseStringEnum.class)) {
            serializer.write(((BaseStringEnum) object).getCode());
        } else {
            serializer.write(object);
        }
    }
}
