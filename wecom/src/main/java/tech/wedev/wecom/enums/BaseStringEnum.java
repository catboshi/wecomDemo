package tech.wedev.wecom.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = BaseStringEnumSerializer.class)
@JsonDeserialize(using = BaseStringEnumDeserializer.class)
public interface BaseStringEnum extends BaseEnum<String>{
    String getCode();

    String getDesc();
}
