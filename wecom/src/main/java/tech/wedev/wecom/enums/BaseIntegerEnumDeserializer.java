package tech.wedev.wecom.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.BeanUtils;

import java.io.IOException;

public class BaseIntegerEnumDeserializer extends JsonDeserializer<BaseIntegerEnum> {
    @Override
    public BaseIntegerEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        final int intValue = p.getIntValue();
        final Class<?> propertyType = BeanUtils.findPropertyType(p.getCurrentName(), p.getCurrentValue().getClass());
        final Object[] enumConstants = propertyType.getEnumConstants();
        for (int i = 0; i < enumConstants.length; i++) {
            final BaseIntegerEnum enumConstant = (BaseIntegerEnum) enumConstants[i];
            if (enumConstant.getCode().equals(intValue)) {
                return enumConstant;
            }
        }
        return null;
    }
}
