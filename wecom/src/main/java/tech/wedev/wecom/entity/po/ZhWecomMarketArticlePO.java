package tech.wedev.wecom.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZhWecomMarketArticlePO implements Serializable {

    private static final long serialVersionUID = -664750447532637009L;

    private String articleTitle;

    private String articleAbstract;

    private String articleLink;

    private String appid;

    private String appLink;

    private String mediaId;
}
