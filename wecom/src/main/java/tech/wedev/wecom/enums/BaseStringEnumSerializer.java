package tech.wedev.wecom.enums;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class BaseStringEnumSerializer extends JsonSerializer<BaseStringEnum> {
    @Override
    public void serialize(BaseStringEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        serializers.defaultSerializeValue(value.getCode(), gen);
    }
}
