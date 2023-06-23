package tech.wedev.wecom.standard.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.wedev.wecom.context.TokenContextHolder;
import tech.wedev.wecom.dao.BasicMapper;
import tech.wedev.wecom.dao.GenParamBasicMapper;
import tech.wedev.wecom.entity.po.GenParamBasicPO;
import tech.wedev.wecom.entity.qo.GenParamBasicQO;
import tech.wedev.wecom.enums.GenParamBasicParamCodeEnum;
import tech.wedev.wecom.enums.GenParamBasicParamTypeEnum;
import tech.wedev.wecom.standard.GenParamBasicService;
import tech.wedev.wecom.utils.ArrayUtils;
import tech.wedev.wecom.utils.BeanUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class GenParamBasicServiceImpl extends BasicServiceImpl<GenParamBasicPO, GenParamBasicQO> implements GenParamBasicService {

    @Autowired
    private GenParamBasicMapper genParamBasicMapper;
    public static Cache<String, String> wecomGenParamCache = CacheBuilder.newBuilder().expireAfterWrite(90, TimeUnit.SECONDS).build();//暂时改成90，原来900

    @Override
    public BasicMapper<GenParamBasicPO, GenParamBasicQO> getBasicMapper() {
        return genParamBasicMapper;
    }

    @Override
    public String queryWecomGenParam(GenParamBasicParamTypeEnum paramType, GenParamBasicParamCodeEnum paramCode) {
        String key = paramType.getCode() + paramCode.getCode();
        String val = wecomGenParamCache.getIfPresent(key);
        if (val == null) {
            val = queryWecomGenParamValue(paramType, paramCode);
            wecomGenParamCache.put(key, val);
        }
        return val;
    }

    @Override
    public String queryWecomGenParamValue(GenParamBasicParamTypeEnum paramType, GenParamBasicParamCodeEnum paramCode) {
        List<GenParamBasicPO> genParamBasicPOS = genParamBasicMapper.select(GenParamBasicQO.builder().paramCode(paramCode).paramType(paramType)
                .orderBys(ArrayUtils.asArrayList("id_0")).corpIds(TokenContextHolder.getCorpIds()).build());
        if (genParamBasicPOS.isEmpty()) {
            return "";
        }
        return genParamBasicPOS.get(0).getParamValue();
    }


    @Override
    public String queryWecomGenParam(GenParamBasicQO genParamBasicQO) {
        String key = genParamBasicQO.getParamType().getCode() + genParamBasicQO.getParamCode().getCode();
        String paramValue = wecomGenParamCache.getIfPresent(key);
        if (paramValue == null) {
            genParamBasicQO.setCorpIds(null);
            genParamBasicQO.setOrderBys(ArrayUtils.asArrayList("id_0"));
            List<GenParamBasicPO> wecomGenParamList = genParamBasicMapper.select(genParamBasicQO);
            paramValue = BeanUtils.defaultIfNull(BeanUtils.defaultGetOne(wecomGenParamList), new GenParamBasicPO()).getParamValue();
            wecomGenParamCache.put(key, paramValue);
        }
        return paramValue;
    }
}