package tech.wedev.wecom.standard;

import tech.wedev.wecom.entity.po.CorpInfo;
import tech.wedev.wecom.entity.qo.CorpInfoQO;

import java.util.List;

public interface CorpInfoMybatisPlusService{

    void update(CorpInfoQO corpInfoQO);

    List<CorpInfo> select();
}