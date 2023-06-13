package tech.wedev.wecom.api.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WechatEventCallBack implements Serializable {

    @Override
    public String toString() {
        return "WechatEventCallBack{" +
                "ToUserName='" + ToUserName + '\'' +
                ", AgentID='" + AgentID + '\'' +
                ", Encrypt='" + Encrypt + '\'' +
                '}';
    }

    /**
     * 企业微信的CorpId
     */
    private String ToUserName;
    /**
     * 接收应用的ID
     */
    private String AgentID;
    private String Encrypt;
}
