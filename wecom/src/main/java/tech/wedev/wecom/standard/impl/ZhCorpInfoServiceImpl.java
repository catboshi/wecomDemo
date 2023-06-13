package tech.wedev.wecom.standard.impl;
import tech.wedev.wecom.standard.ZhCorpInfoService;
import tech.wedev.wecom.entity.po.ZhCorpInfo;
import tech.wedev.wecom.entity.qo.ZhCorpInfoQO;
import tech.wedev.wecom.dao.BasicMapper;
import tech.wedev.wecom.dao.ZhCorpInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ZhCorpInfoServiceImpl extends BasicServiceImpl<ZhCorpInfo,ZhCorpInfoQO> implements ZhCorpInfoService {

    @Autowired
    private ZhCorpInfoMapper zhCorpInfoMapper;
    @Override
    public BasicMapper<ZhCorpInfo,ZhCorpInfoQO> getBasicMapper(){
        return zhCorpInfoMapper;
    }
}