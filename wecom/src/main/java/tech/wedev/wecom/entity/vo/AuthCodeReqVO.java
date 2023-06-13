package tech.wedev.wecom.entity.vo;

import tech.wedev.wecom.annos.NotBlank;
import lombok.Data;

@Data
public class AuthCodeReqVO {

    @NotBlank
    private String mobile;

    @NotBlank
    private String name;
}
