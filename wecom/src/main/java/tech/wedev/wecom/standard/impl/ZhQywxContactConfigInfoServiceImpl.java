package tech.wedev.wecom.standard.impl;
import tech.wedev.wecom.standard.ZhQywxContactConfigInfoService;
import tech.wedev.wecom.entity.po.ZhQywxContactConfigInfoPO;
import tech.wedev.wecom.entity.qo.ZhQywxContactConfigInfoQO;
import tech.wedev.wecom.dao.BasicMapper;
import tech.wedev.wecom.dao.ZhQywxContactConfigInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ZhQywxContactConfigInfoServiceImpl extends BasicServiceImpl<ZhQywxContactConfigInfoPO,ZhQywxContactConfigInfoQO> implements ZhQywxContactConfigInfoService {

    @Autowired
    private ZhQywxContactConfigInfoMapper zhQywxContactConfigInfoMapper;
    @Override
    public BasicMapper<ZhQywxContactConfigInfoPO,ZhQywxContactConfigInfoQO> getBasicMapper(){
        return zhQywxContactConfigInfoMapper;
    }
}