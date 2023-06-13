package tech.wedev.wecom.standard.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.wedev.wecom.context.TokenContextHolder;
import tech.wedev.wecom.dao.BaseMapper;
import tech.wedev.wecom.dao.GenParamMapper;
import tech.wedev.wecom.entity.po.GenParamPO;
import tech.wedev.wecom.entity.qo.GenParamQO;
import tech.wedev.wecom.enums.BaseDeletedEnum;
import tech.wedev.wecom.enums.GenParamEnum;
import tech.wedev.wecom.enums.JwtKeyEnum;
import tech.wedev.wecom.standard.GenParamService;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @see GenParamBasicServiceImpl
 */
@Slf4j
@Service
@Deprecated
public class GenParamServiceImpl extends BaseServiceImpl<GenParamPO, GenParamQO> implements GenParamService {

    public static Cache<String, String> wecomGenParamCache = CacheBuilder.newBuilder().expireAfterWrite(900, TimeUnit.SECONDS).build();

    @Autowired
    GenParamMapper genParamMapper;

    @Override
    @Deprecated
    public String queryWecomGenParamValue(GenParamEnum genParamEnum) {
        GenParamQO genParamQO = new GenParamQO();
        genParamQO.setParamType(genParamEnum.getParamTypeEnum().getName());
        genParamQO.setParamCode(genParamEnum.getName());
        genParamQO.setIsDeleted(BaseDeletedEnum.EXISTS.getCode());
        //通过枚举精确查找，可以不用加
        genParamQO.setCorpId(TokenContextHolder.getValue(JwtKeyEnum.CORP_ID, String.class));
        List<GenParamPO> list = genParamMapper.select(genParamQO);
        if (list.isEmpty()) {
            return "";
        }
        GenParamPO genParamPO = list.get(0);
        return genParamPO.getParamValue();
    }

    @Override
    public int updateWecomGenParam(GenParamPO genParamPO) {
        GenParamQO genParamQO = new GenParamQO();
        genParamQO.setParamType(genParamPO.getParamType());
        genParamQO.setParamCode(genParamPO.getParamCode());
        genParamQO.setIsDeleted(BaseDeletedEnum.EXISTS.getCode());
        genParamQO.setParamValue(genParamPO.getParamValue());
        genParamQO.setGmtModified(new Date());
        genParamQO.setCorpId(TokenContextHolder.getValue(JwtKeyEnum.CORP_ID, String.class));
        return genParamMapper.updateWecomGenParam(genParamQO);
    }

    @Override
    @Deprecated
    public String queryWecomGenParam(GenParamPO genParamPO) {
        log.info("GenParamService###queryWecomGenParam入参" + JSON.toJSONString(genParamPO));
        String key = genParamPO.getParamType() + genParamPO.getParamCode();
        if (wecomGenParamCache.getIfPresent(key)==null) {
            try {
                genParamPO.setCorpId(TokenContextHolder.getValue(JwtKeyEnum.CORP_ID, String.class));
                List<GenParamPO> wecomGenParamList = genParamMapper.selectByParam(genParamPO);
                wecomGenParamCache.put(key, wecomGenParamList.get(0).getParamValue());
            } catch (Exception e) {
                log.error("GenParamService###queryWecomGenParam查询失败",e);
            }
        }
        log.info("GenParamService###queryWecomGenParam出参"+wecomGenParamCache.getIfPresent(key));
        return wecomGenParamCache.getIfPresent(key);
    }

    @Override
    public String queryWecomGenParam(GenParamQO genParamQO) {
        log.info("GenParamService###queryWecomGenParam入参" + JSON.toJSONString(genParamQO));
        String key = genParamQO.getParamType() + genParamQO.getParamCode();
        if (wecomGenParamCache.getIfPresent(key)==null) {
            try {
                genParamQO.setCorpId(TokenContextHolder.getValue(JwtKeyEnum.CORP_ID, String.class));
                List<GenParamQO> wecomGenParamList = genParamMapper.selectByParams(genParamQO);
                wecomGenParamCache.put(key, wecomGenParamList.get(0).getParamValue());
            } catch (Exception e) {
                log.error("GenParamService###queryWecomGenParam查询失败",e);
            }
        }
        log.info("GenParamService###queryWecomGenParam出参"+wecomGenParamCache.getIfPresent(key));
        return wecomGenParamCache.getIfPresent(key);
    }

    @Override
    @Deprecated
    public String queryWecomGenParam(GenParamEnum genParamEnum) {
        log.info("GenParamService###queryWecomGenParam入参" + JSON.toJSONString(genParamEnum));
        String key = genParamEnum.getParamTypeEnum().getName() + genParamEnum.getName();
        if (wecomGenParamCache.getIfPresent(key)==null) {
            try {
                String paramValue = genParamMapper.selectValueByUK(genParamEnum, TokenContextHolder.getValue(JwtKeyEnum.CORP_ID, String.class));
                wecomGenParamCache.put(key, paramValue);
            } catch (Exception e) {
                log.error("GenParamService###queryWecomGenParam查询失败",e);
            }
        }
        log.info("GenParamService###queryWecomGenParam出参"+wecomGenParamCache.getIfPresent(key));
        return wecomGenParamCache.getIfPresent(key);
    }

    @Override
    public List<GenParamPO> queryWecomGenParamValueByType(String paramType) {
        GenParamQO genParamQO = new GenParamQO();
        genParamQO.setParamType(paramType);
        genParamQO.setIsDeleted(BaseDeletedEnum.EXISTS.getCode());
        genParamQO.setCorpId(TokenContextHolder.getValue(JwtKeyEnum.CORP_ID, String.class));
        List<GenParamPO> list = genParamMapper.select(genParamQO);
        return list;
    }

    @Override
    public BaseMapper<GenParamPO, GenParamQO> getBaseMapper() {
        return genParamMapper;
    }
}

