package tech.wedev.wecom.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.BeanUtils;

import java.io.IOException;

public class BaseStringEnumDeserializer extends JsonDeserializer<BaseStringEnum> {
    @Override
    public BaseStringEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final String value = p.getValueAsString();
        final Class<?> propertyType = BeanUtils.findPropertyType(p.getCurrentName(), p.getCurrentValue().getClass());
        final Object[] enumConstants = propertyType.getEnumConstants();
        for (int i = 0; i < enumConstants.length; i++) {
            final BaseStringEnum enumConstant = (BaseStringEnum) enumConstants[i];
            if (enumConstant.getCode().equals(value)) {
                return enumConstant;
            }
        }
        return null;
    }
}
