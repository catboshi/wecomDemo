package tech.wedev.wecom.standard;

import lombok.SneakyThrows;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import tech.wedev.wecom.bean.PageBean;
import tech.wedev.wecom.entity.po.BasicPO;
import tech.wedev.wecom.entity.qo.BasicQO;

import java.util.List;

public interface BasicService<P extends BasicPO, Q extends BasicQO> {
    Long statisticsExists(Q q);

    List<P> select(Q q);

    P selectOne(Q q);

    P selectOneExists(Q q);

    List<P> selectExists(Q q);

    @SneakyThrows
    P selectById(Long id);

    @SneakyThrows
    List<P> selectByIds(List<Long> ids);

    @SneakyThrows
    P selectExistsById(Long id);

    @SneakyThrows
    List<P> selectExistsByIds(List<Long> ids);

    PageBean<P> selectPage(Q q);

    Integer updateSelective(Q q);

//    void saveAfter(P p);

    @Transactional
    Integer batchUpdateSelective(List<Q> qs);

    Integer update(Q q);

    @Transactional
    Integer batchUpdate(List<Q> qs);

    Integer deleteLogicByIds(List<Long> ids);

    Integer deleteLogicById(Long id);

    Integer deleteByLogic(Q q);

    Integer delete(Q q);

    Integer save(P p);

    Long saveReturnId(@RequestBody P p);

    Integer batchSave(List<P> ps);

    Integer batchInsert(List<P> ps);

    Long statistics(Q q);
}

