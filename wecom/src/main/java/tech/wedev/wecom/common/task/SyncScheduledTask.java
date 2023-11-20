package tech.wedev.wecom.common.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.wedev.wecom.entity.qo.CorpInfoQO;
import tech.wedev.wecom.standard.CorpInfoMybatisPlusService;

@Slf4j
@Component
public class SyncScheduledTask implements Runnable{
    private CorpInfoMybatisPlusService corpInfoMybatisPlusService;
    public SyncScheduledTask(CorpInfoMybatisPlusService corpInfoMybatisPlusService){
        this.corpInfoMybatisPlusService=corpInfoMybatisPlusService;
    }

    @Override
    public void run(){
        corpInfoMybatisPlusService.update(new CorpInfoQO());
    }
}
