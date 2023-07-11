package tech.wedev.wecom.standard.impl;
import tech.wedev.wecom.standard.QywxContactConfigInfoService;
import tech.wedev.wecom.entity.po.QywxContactConfigInfoPO;
import tech.wedev.wecom.entity.qo.QywxContactConfigInfoQO;
import tech.wedev.wecom.dao.BasicMapper;
import tech.wedev.wecom.dao.QywxContactConfigInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class QywxContactConfigInfoServiceImpl extends BasicServiceImpl<QywxContactConfigInfoPO,QywxContactConfigInfoQO> implements QywxContactConfigInfoService {

    @Autowired
    private QywxContactConfigInfoMapper qywxContactConfigInfoMapper;

    @Override
    public BasicMapper<QywxContactConfigInfoPO,QywxContactConfigInfoQO> getBasicMapper(){
        return qywxContactConfigInfoMapper;
    }
}