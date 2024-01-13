package tech.wedev.wecom.standard.impl;

import com.github.pagehelper.page.PageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.wedev.wecom.mybatis.mapper.BasicMapper;
import tech.wedev.wecom.mybatis.mapper.CustMgrMapMapper;
import tech.wedev.wecom.entity.po.CustMgrMapPO;
import tech.wedev.wecom.entity.qo.CustMgrMapQO;
import tech.wedev.wecom.standard.CustMgrMapService;

import java.util.List;


@Service
public class CustMgrMapServiceImpl extends BasicServiceImpl<CustMgrMapPO, CustMgrMapQO> implements CustMgrMapService {

    @Autowired
    private CustMgrMapMapper custMgrMapMapper;

    @Override
    public BasicMapper<CustMgrMapPO, CustMgrMapQO> getBasicMapper() {
        return custMgrMapMapper;
    }

    @Override
    public List<CustMgrMapPO> selectList() {
        PageMethod.startPage(1, 30);
        return custMgrMapMapper.selectList();
    }
}