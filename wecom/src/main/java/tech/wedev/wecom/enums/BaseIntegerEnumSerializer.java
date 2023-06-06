package tech.wedev.wecom.enums;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class BaseIntegerEnumSerializer extends JsonSerializer<BaseIntegerEnum> {
    @Override
    public void serialize(BaseIntegerEnum value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        serializerProvider.defaultSerializeValue(value.getCode(), gen);
    }
}
