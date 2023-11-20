package tech.wedev.wecom.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class CommonScheduledThreadPool{

    @Bean
    public ScheduledExecutorService scheduledExecutorTask(){
        return new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
