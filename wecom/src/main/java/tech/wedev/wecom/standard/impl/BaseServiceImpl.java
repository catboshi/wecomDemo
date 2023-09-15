package tech.wedev.wecom.standard.impl;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.wedev.wecom.bean.PageBean;
import tech.wedev.wecom.entity.po.BasePO;
import tech.wedev.wecom.entity.qo.BaseQO;
import tech.wedev.wecom.enums.BaseDeletedEnum;
import tech.wedev.wecom.exception.ExceptionAssert;
import tech.wedev.wecom.exception.ExceptionCode;
import tech.wedev.wecom.standard.BaseService;
import tech.wedev.wecom.utils.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * @see BasicServiceImpl
 * @param <P>
 * @param <Q>
 */
@Deprecated
public abstract class BaseServiceImpl<P extends BasePO, Q extends BaseQO> implements BaseService<P,Q> {
    @Override
    @Transactional
    public P selectByPrimaryKey(Q q) {
        ExceptionAssert.isNull(q.getId(), ExceptionCode.INVALID_PARAMETER);
        List<P> select = getBaseMapper().select(q);
        ExceptionAssert.isTrue(select.size() > 1, ExceptionCode.MORE_THAN_ONE);
        return BeanUtils.defaultGetOne(select);
    }

    @Override
    @Transactional
    public P selectExistsByPrimaryKEy(Q q) {
        ExceptionAssert.isNull(q.getId(), ExceptionCode.INVALID_PARAMETER);
        q.setIsDeleted(BaseDeletedEnum.EXISTS.getCode());
        List<P> select = this.getBaseMapper().select(q);
        ExceptionAssert.isTrue(select.size() > 1, ExceptionCode.MORE_THAN_ONE);
        return BeanUtils.defaultGetOne(select);
    }

    @Override
    @Transactional
    public P selectByKey(Long id) {
        ExceptionAssert.isNull(id, ExceptionCode.INVALID_PARAMETER);
        final Q q = getQObject();
        q.setId(id);
        List<P> select = this.getBaseMapper().select(q);
        ExceptionAssert.isTrue(select.size() > 1, ExceptionCode.MORE_THAN_ONE);
        return BeanUtils.defaultGetOne(select);
    }

    private Q getQObject() {
        final String simpleName = this.getClass().getSimpleName();
        try {
            String beanClassName = BaseQO.class.getName().replaceAll("BaseQO", simpleName.substring(0, simpleName.indexOf("ServiceImpl")) + "QO");
            return (Q) Class.forName(beanClassName).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public P selectExistsByKey(Long id) {
        ExceptionAssert.isNull(id, ExceptionCode.INVALID_PARAMETER);
        final Q q = getQObject();
        q.setId(id);
        q.setIsDeleted(BaseDeletedEnum.EXISTS.getCode());
        List<P> select = this.getBaseMapper().select(q);
        ExceptionAssert.isTrue(select.size() > 1, ExceptionCode.MORE_THAN_ONE);
        return BeanUtils.defaultGetOne(select);    }

    @Override
    @Transactional
    public PageBean<P> selectPage(Q q) {
        q.setPageSize(BeanUtils.defaultIfNull(q.getPageSize(), 10));
        q.setPageNum(BeanUtils.defaultIfNull(q.getPageNum(), 1));
        final List<P> select = this.getBaseMapper().select(q);
        if (select.size()==2) {
            final List<P> p1 = (List<P>) select.get(0);
            return PageBean.build(q.getPageSize(), q.getPageNum(), ((List<Integer>)select.get(1)).get(0), p1);
        }
        return PageBean.build(q.getPageSize(), q.getPageNum(), select);
    }

    @Override
    @Transactional
    public List<P> select(Q q) {
        q.setPageNum(null);
        q.setPageSize(null);
        return this.getBaseMapper().select(q);
    }

    @Override
    @Transactional
    public int deleteByPrimaryKey(Q q) {
        ExceptionAssert.isNull(q.getId(), ExceptionCode.INVALID_PARAMETER);
        return this.getBaseMapper().batchDeleteByPrimaryKey(q);
    }

    @Override
    @Transactional
    public int deleteByPrimaryKeyForLogic(Q q) {
        q.setIsDeleted(BaseDeletedEnum.DEL.getCode());
        q.setGmtModified(BeanUtils.defaultIfNull(q.getGmtModified(), new Date()));
        return this.updateByPrimaryKey(q);
    }

    @Override
    @Transactional
    public int updateByPrimaryKey(Q q) {
        ExceptionAssert.isTrue(q.getId()==null&& CollectionUtils.isEmpty(q.getIds()), ExceptionCode.INVALID_PARAMETER);
        this.updateByPrimaryKeyBefore(q);
        q.setGmtModified(BeanUtils.defaultIfNull(q.getGmtModified(), new Date()));
        return this.getBaseMapper().updateByPrimaryKey(q);
    }

    @Override
    @Transactional
    public int save(P p) {
        this.saveBefore(p);
        p.setIsDeleted(BaseDeletedEnum.EXISTS.getCode());
        p.setGmtCreate(BeanUtils.defaultIfNull(p.getGmtCreate(), new Date()));
        p.setGmtModified(BeanUtils.defaultIfNull(p.getGmtModified(), new Date()));
        return this.getBaseMapper().save(p);
    }
}
