package tech.wedev.wecom.common.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import tech.wedev.wecom.common.task.SyncScheduledTask;
import tech.wedev.wecom.standard.CorpInfoMybatisPlusService;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class SyncWecomStatusRunner implements ApplicationRunner {

    private final ScheduledExecutorService executor;
    private final CorpInfoMybatisPlusService corpInfoMybatisPlusService;

    @Override
    public void run(ApplicationArguments args){
        executor.scheduleAtFixedRate(new SyncScheduledTask(corpInfoMybatisPlusService), 60, 30, TimeUnit.SECONDS);
    }

}
