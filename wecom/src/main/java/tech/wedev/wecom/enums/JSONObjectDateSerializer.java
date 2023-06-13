package tech.wedev.wecom.enums;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import tech.wedev.wecom.utils.DateUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

public class JSONObjectDateSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        serializer.write(DateUtils.formatDateToStr((Date) object, "yyyy-MM-dd HH:mm:ss"));
    }
}
