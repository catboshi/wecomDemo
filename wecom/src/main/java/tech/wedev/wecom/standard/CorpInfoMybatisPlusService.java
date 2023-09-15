package tech.wedev.wecom.standard;

import tech.wedev.wecom.entity.po.CorpInfoMyPlus;
import tech.wedev.wecom.entity.qo.CorpInfoQO;

import java.util.List;

public interface CorpInfoMybatisPlusService{

    void update(CorpInfoQO corpInfoQO);

    List<CorpInfoMyPlus> select();
}