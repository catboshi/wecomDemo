package tech.wedev.wecom.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = BaseIntegerEnumSerializer.class)
@JsonDeserialize(using = BaseIntegerEnumDeserializer.class)
public interface BaseIntegerEnum extends BaseEnum<Integer>{

    Integer getCode();

    String getDesc();

}
