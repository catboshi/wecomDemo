package tech.wedev.wecom.mapper;

import org.apache.ibatis.annotations.*;
import tech.wedev.wecom.entity.po.BasicPO;
import tech.wedev.wecom.entity.qo.BasicQO;
import tech.wedev.wecom.mapper.provider.CommonProvider;

import java.math.BigDecimal;
import java.util.List;

public interface BasicMapper<P extends BasicPO, Q extends BasicQO> {
    @SelectProvider(type = CommonProvider.class, method = "select")
    List<P> select(Q q);

    @SelectProvider(type = CommonProvider.class, method = "selectPage")
    List<P> selectPage(Q q);

    @SelectProvider(type = CommonProvider.class, method = "getTotal")
    Integer getTotal();

    @UpdateProvider(type = CommonProvider.class, method = "update")
    Integer update(Q q);

    @DeleteProvider(type = CommonProvider.class, method = "delete")
    Integer delete(Q q);

    @DeleteProvider(type = CommonProvider.class, method = "batchDeleteByPrimaryKey")
    Integer batchDeleteByPrimaryKey(Q q);

    @InsertProvider(type = CommonProvider.class, method = "insert")
    @SelectKey(keyProperty = "id", keyColumn = "id", before = false, resultType = Lang.class, statement = "select last_insert_id()")
    Integer save(P p);

    @InsertProvider(type = CommonProvider.class, method = "batchInsert")
    @SelectKey(keyProperty = "ps[0].id", keyColumn = "id", before = false, resultType = Lang.class, statement = "select last_insert_id()")
    Integer batchSave(@Param("ps") List<P> ps, @Param("p") Class<P> pClass);

    @UpdateProvider(type = CommonProvider.class, method = "updateSelective")
    Integer updateSelective(Q q);

    @UpdateProvider(type = CommonProvider.class, method = "updateByPrimaryKey")
    Integer updateByPrimaryKey(Q q);

    @SelectProvider(type = CommonProvider.class, method = "statistics")
    Long statistics(Q q);

    @SelectProvider(type = CommonProvider.class, method = "statistics")
    BigDecimal statisticsMix(Q q);
}
