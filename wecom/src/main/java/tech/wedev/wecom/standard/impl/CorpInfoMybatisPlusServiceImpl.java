package tech.wedev.wecom.standard.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import tech.wedev.wecom.entity.po.CorpInfo;
import tech.wedev.wecom.entity.qo.CorpInfoQO;
import tech.wedev.wecom.mybatisplus.mapper.CorpInfoMybatisPlusMapper;
import tech.wedev.wecom.standard.CorpInfoMybatisPlusService;

import java.util.Date;
import java.util.List;

@Service
public class CorpInfoMybatisPlusServiceImpl extends ServiceImpl<CorpInfoMybatisPlusMapper, CorpInfo> implements CorpInfoMybatisPlusService {


    @Override
    public void update(CorpInfoQO corpInfoQO) {
        List<CorpInfo> list = this.lambdaQuery()
                .eq(CorpInfo::getCorpId, corpInfoQO.getCorpId())
                .eq(CorpInfo::getIsDelete, false)
                .list();
        if (CollectionUtils.isNotEmpty(list)) {
            //更新
            this.lambdaUpdate().eq(CorpInfo::getCorpId, corpInfoQO.getCorpId())
                    .eq(CorpInfo::getIsDelete, false)
                    .set(CorpInfo::getAgentApplication, corpInfoQO.getAgentApplication())
                    .set(CorpInfo::getSecretApplication, corpInfoQO.getSecretApplication())
                    .set(CorpInfo::getSecretCommunication, corpInfoQO.getSecretCommunication())
                    .set(CorpInfo::getSecretExternalContact, corpInfoQO.getSecretExternalContact())
                    .set(CorpInfo::getGmtModified, new Date())
                    .set(CorpInfo::getModifiedId, 555114100L)
                    .update();
            return;
        }
        //保存
        this.save(CorpInfo.builder().corpId(corpInfoQO.getCorpId()).agentApplication(corpInfoQO.getAgentApplication()).secretApplication(corpInfoQO.getSecretApplication())
                .secretCommunication(corpInfoQO.getSecretCommunication())
                .secretExternalContact(corpInfoQO.getSecretExternalContact())
                .build());
    }

    @Override
    public List<CorpInfo> select() {
        PageMethod.startPage(1, 30);
        return this.lambdaQuery()
                .eq(CorpInfo::getCorpId, "wwbda3d4206748805c")
                .eq(CorpInfo::getIsDelete, false)
                .list();
    }
}