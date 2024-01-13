package tech.wedev.wecom.standard.impl;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import tech.wedev.wecom.bean.PageBean;
import tech.wedev.wecom.entity.po.BasicPO;
import tech.wedev.wecom.entity.qo.BasicQO;
import tech.wedev.wecom.enums.*;
import tech.wedev.wecom.mybatis.mapper.BasicMapper;
import tech.wedev.wecom.standard.BasicService;
import tech.wedev.wecom.utils.BeanUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class BasicServiceImpl<P extends BasicPO, Q extends BasicQO> implements BasicService<P, Q> {
    private static final String PO_PACKAGE = "tech.wedev.wecom.dao.po";
    private static final String ENUM_PACKAGE = "tech.wedev.wecom.enums";
    private SerializeConfig globalInstance = SerializeConfig.globalInstance;
    private Class<P> pClass;
    private Class<Q> qClass;

    public abstract BasicMapper<P, Q> getBasicMapper();

    public Class<P> getpClass() {
        return pClass;
    }

    public Class<Q> getqClass() {
        return qClass;
    }

    @Override
    @Transactional
    public Long statistics(Q q) {
        return this.getBasicMapper().statistics(q);
    }

    @Override
    @Transactional
    public Long statisticsExists(Q q) {
        q.setIsDeleted(BaseDeletedEnum.EXISTS);
        return this.getBasicMapper().statistics(q);
    }

    @PostConstruct
    @SneakyThrows
    public void init() {
        Reflections reflections = new Reflections(ENUM_PACKAGE);
        JSONObjectBaseEnumSerializer jsonObjectBaseEnumSerializer = new JSONObjectBaseEnumSerializer();

        Set<Class<? extends BaseIntegerEnum>> subTypesOfBaseIntegerEnum = reflections.getSubTypesOf(BaseIntegerEnum.class);
        Set<Class<? extends BaseStringEnum>> subTypesOfBaseStringEnum = reflections.getSubTypesOf(BaseStringEnum.class);
        for (Class<? extends BaseIntegerEnum> aClass : subTypesOfBaseIntegerEnum) {
            globalInstance.put(aClass, jsonObjectBaseEnumSerializer);
        }
        for (Class<? extends BaseStringEnum> aClass : subTypesOfBaseStringEnum) {
            globalInstance.put(aClass, jsonObjectBaseEnumSerializer);
        }
        ToStringSerializer value = new ToStringSerializer();
        globalInstance.put(Long.class, value);
        globalInstance.put(Long.TYPE, value);
        globalInstance.put(long.class, value);
        //https://zhidao.baidu.com/question/89919374.html
        globalInstance.put(Date.class, new JSONObjectDateSerializer());
        Type[] actualTypeArguments = ((ParameterizedTypeImpl) this.getClass().getGenericSuperclass()).getActualTypeArguments();
        for (Type actualTypeArgument : actualTypeArguments) {
            String typeName = actualTypeArgument.getTypeName();
            if (typeName.endsWith("QO")) {
                this.qClass = (Class<Q>) Class.forName(typeName);
            } else if (typeName.endsWith("PO")) {
                this.pClass = (Class<P>) Class.forName(typeName);
            }
        }
    }

    @Override
    @Transactional
    public List<P> select(Q q) {
        return this.getBasicMapper().select(q);
    }

    @Override
    @Transactional
    public P selectOne(Q q) {
        return BeanUtils.defaultGetOne(this.getBasicMapper().select(q));
    }

    @Override
    @Transactional
    public P selectOneExists(Q q) {
        q.setIsDeleted(BaseDeletedEnum.EXISTS);
        return BeanUtils.defaultGetOne(this.getBasicMapper().select(q));
    }

    @Override
    @Transactional
    public List<P> selectExists(Q q) {
        q.setIsDeleted(BaseDeletedEnum.EXISTS);
        return this.getBasicMapper().select(q);
    }

    @SneakyThrows
    @Override
    @Transactional
    public P selectById(Long id) {
        Q q = this.qClass.newInstance();
        q.setId(id);
        return BeanUtils.defaultGetOne(this.select(q));
    }

    @SneakyThrows
    @Override
    @Transactional
    public List<P> selectByIds(List<Long> ids) {
        Q q = this.qClass.newInstance();
        q.setIds(ids);
        return this.select(q);
    }

    @Override
    @SneakyThrows
    @Transactional
    public P selectExistsById(Long id) {
        Q q = this.qClass.newInstance();
        q.setId(id);
        q.setIsDeleted(BaseDeletedEnum.EXISTS);
        return BeanUtils.defaultGetOne(this.getBasicMapper().select(q));
    }

    @Override
    @SneakyThrows
    @Transactional
    public List<P> selectExistsByIds(List<Long> ids) {
        Q q = this.qClass.newInstance();
        q.setIds(ids);
        q.setIsDeleted(BaseDeletedEnum.EXISTS);
        return this.getBasicMapper().select(q);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageBean<P> selectPage(Q q) {
        q.setPageSize(BeanUtils.defaultIfNull(q.getPageSize(), 10));
        q.setPageNum(BeanUtils.defaultIfNull(q.getPageNum(), 1));
        List<P> ps = this.getBasicMapper().selectPage(q);
        Integer total = this.getBasicMapper().getTotal();
        return PageBean.build(q.getPageSize(), q.getPageNum(), total, ps);
    }

    @Override
    @Transactional
    public Integer updateSelective(Q q) {
        return this.getBasicMapper().updateSelective(q);
    }

    @Override
    @Transactional
    public Integer batchUpdateSelective(List<Q> qs) {
        int i = 0;
        for (Q q : qs) {
            i += this.getBasicMapper().updateSelective(q);
        }
        return i;
    }

    @Override
    @Transactional
    public Integer update(Q q) {
        return this.getBasicMapper().update(q);
    }

    @Override
    @Transactional
    public Integer batchUpdate(List<Q> qs) {
        int i = 0;
        for (Q q : qs) {
            i += this.getBasicMapper().update(q);
        }
        return i;
    }

    @Override
    @SneakyThrows
    @Transactional
    public Integer deleteLogicByIds(List<Long> ids) {
        BasicQO baseQO = this.qClass.newInstance();
        BasicPO basePO = this.pClass.newInstance();
        if (baseQO != null) {
            if (basePO != null) {
                baseQO.setUpdateValPO(basePO);
                basePO.setGmtModified(Optional.ofNullable(basePO).map(BasicPO::getGmtModified).orElse(new Date()));
                basePO.setIsDeleted(BaseDeletedEnum.DEL.getCode());
            }
            baseQO.setIds(ids);
            return this.getBasicMapper().updateSelective((Q) baseQO);
        }
        return 0;
    }

    @Override
    @SneakyThrows
    @Transactional
    public Integer deleteLogicById(Long id) {
        BasicQO baseQO = this.qClass.newInstance();
        BasicPO basePO = this.pClass.newInstance();
        if (baseQO != null) {
            if (basePO != null) {
                baseQO.setUpdateValPO(basePO);
                basePO.setGmtModified(Optional.ofNullable(basePO).map(a -> a.getGmtModified()).orElse(new Date()));
                basePO.setIsDeleted(BaseDeletedEnum.DEL.getCode());
            }
            baseQO.setId(id);
            return this.getBasicMapper().updateSelective((Q) baseQO);
        }
        return 0;
    }

    @Override
    @SneakyThrows
    @Transactional
    public Integer deleteByLogic(Q q) {
        BasicPO updateValPO = q.getUpdateValPO() == null ? pClass.newInstance() : q.getUpdateValPO();
        q.setUpdateValPO(updateValPO);
        updateValPO.setIsDeleted(BaseDeletedEnum.DEL.getCode());
        updateValPO.setGmtModified(updateValPO.getModifiedId() == null ? new Date() : updateValPO.getGmtModified());
        return this.getBasicMapper().updateSelective(q);
    }

    @Override
    @Transactional
    public Integer delete(Q q) {
        return this.getBasicMapper().delete(q);
    }

    @Override
    @Transactional
    public Integer save(P p) {
        return this.getBasicMapper().save(p);
    }

    @Override
    @Transactional
    public Long saveReturnId(@RequestBody P p) {
        this.getBasicMapper().save(p);
        return p.getId();
    }

    @Override
    @Transactional
    public Integer batchSave(List<P> ps) {
        int i = 0;
        for (P p : ps) {
            i += this.getBasicMapper().save(p);
        }
        return i;
    }

    /**
     * 小规模批量保存时使用
     *
     * @param ps
     * @return
     */
    @Override
    @Transactional
    public Integer batchInsert(List<P> ps) {
        Integer total = this.getBasicMapper().batchSave(ps, this.getpClass());
        ps.stream().findFirst().map(BasicPO::getId).ifPresent(id -> {
            for (int i = 1; i < ps.size(); i++) {
                ps.get(i).setId(id + i);
            }
        });
        return total;
    }
}

