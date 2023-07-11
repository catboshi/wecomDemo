package tech.wedev.wecom.standard.impl;
import tech.wedev.wecom.standard.CorpInfoService;
import tech.wedev.wecom.entity.po.CorpInfo;
import tech.wedev.wecom.entity.qo.CorpInfoQO;
import tech.wedev.wecom.dao.BasicMapper;
import tech.wedev.wecom.dao.CorpInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CorpInfoServiceImpl extends BasicServiceImpl<CorpInfo,CorpInfoQO> implements CorpInfoService {

    @Autowired
    private CorpInfoMapper corpInfoMapper;
    @Override
    public BasicMapper<CorpInfo,CorpInfoQO> getBasicMapper(){
        return corpInfoMapper;
    }
}