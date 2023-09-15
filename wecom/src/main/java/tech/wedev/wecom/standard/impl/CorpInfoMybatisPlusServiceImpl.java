package tech.wedev.wecom.standard.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import tech.wedev.wecom.entity.po.CorpInfoMyPlus;
import tech.wedev.wecom.entity.qo.CorpInfoQO;
import tech.wedev.wecom.mybatisplus.mapper.CorpInfoMybatisPlusMapper;
import tech.wedev.wecom.standard.CorpInfoMybatisPlusService;

import java.util.Date;
import java.util.List;

@Service
public class CorpInfoMybatisPlusServiceImpl extends ServiceImpl<CorpInfoMybatisPlusMapper, CorpInfoMyPlus> implements CorpInfoMybatisPlusService {


    @Override
    public void update(CorpInfoQO corpInfoQO) {
        List<CorpInfoMyPlus> list = this.lambdaQuery()
                .eq(CorpInfoMyPlus::getCorpId, corpInfoQO.getCorpId())
                .eq(CorpInfoMyPlus::getIsDelete, false)
                .list();
        if (CollectionUtils.isNotEmpty(list)) {
            //更新
            this.lambdaUpdate().eq(CorpInfoMyPlus::getCorpId, corpInfoQO.getCorpId())
                    .eq(CorpInfoMyPlus::getIsDelete, false)
                    .set(CorpInfoMyPlus::getAgentApplication, corpInfoQO.getAgentApplication())
                    .set(CorpInfoMyPlus::getSecretApplication, corpInfoQO.getSecretApplication())
                    .set(CorpInfoMyPlus::getSecretCommunication, corpInfoQO.getSecretCommunication())
                    .set(CorpInfoMyPlus::getSecretExternalContact, corpInfoQO.getSecretExternalContact())
                    .set(CorpInfoMyPlus::getGmtModified, new Date())
                    .set(CorpInfoMyPlus::getModifiedId, 555114100L)
                    .update();
            return;
        }
        //保存
        this.save(CorpInfoMyPlus.builder().corpId(corpInfoQO.getCorpId()).agentApplication(corpInfoQO.getAgentApplication()).secretApplication(corpInfoQO.getSecretApplication())
                .secretCommunication(corpInfoQO.getSecretCommunication())
                .secretExternalContact(corpInfoQO.getSecretExternalContact())
                .build());
    }

    @Override
    public List<CorpInfoMyPlus> select() {
        return this.lambdaQuery()
                .eq(CorpInfoMyPlus::getCorpId, "wwbda3d4206748805c")
                .eq(CorpInfoMyPlus::getIsDelete, false)
                .list();
    }
}