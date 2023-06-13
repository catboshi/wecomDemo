package tech.wedev.wecom.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AccessCredentialsCommand implements Serializable {

    private static final long serialVersionUID = 3547416444669652959L;

    /**
     * 企微API接口的标识URL
     */
    private String interfaceIdentifyUrl;

    private String corpId;

    private String eventCode;

    private String orgCode;

    private String methodType;

    private String trxcode;

    private String ticketType;

}
