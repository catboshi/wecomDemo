package tech.wedev.wecom.standard;

import tech.wedev.wecom.bean.PageBean;
import tech.wedev.wecom.mybatis.mapper.BaseMapper;
import tech.wedev.wecom.entity.po.BasePO;
import tech.wedev.wecom.entity.qo.BaseQO;

import java.util.List;

/**
 * @see BasicService
 * @param <P>
 * @param <Q>
 */
@Deprecated
public interface BaseService<P extends BasePO, Q extends BaseQO> {
    P selectByPrimaryKey(Q q);

    P selectExistsByPrimaryKEy(Q q);

    P selectByKey(Long id);

    P selectExistsByKey(Long id);

    PageBean<P> selectPage(Q q);

    List<P> select(Q q);

    int deleteByPrimaryKey(Q q);

    int deleteByPrimaryKeyForLogic(Q q);

    int updateByPrimaryKey(Q q);

    default void updateByPrimaryKeyBefore(Q q) {

    }

    int save(P p);

    default void saveBefore(P p) {

    }

    BaseMapper<P, Q> getBaseMapper();
}
