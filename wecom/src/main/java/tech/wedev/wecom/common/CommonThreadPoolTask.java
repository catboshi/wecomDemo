package tech.wedev.wecom.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class CommonThreadPoolTask {

    /** 核心线程数（默认线程数）*/
    private static final int CORE_POOL_SIZE = 5;
    /** 最大线程数 */
    private static final int MAX_POOL_SIZE = 20;
    /** 允许线程空闲时间（单位：默认为秒）*/
    private static final int KEEP_ALIVE_TIME = 60;
    /** 缓冲队列大小 */
    private static final int QUEUE_CAPACITY = 200;
    /** 线程池名前缀 */
    private static final String THREAD_NAME_PREFIX = "wecom-async-thread-pool-";

    @Bean("asyncTaskExecutor")
    public ThreadPoolTaskExecutor initializeThreadPool(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();//spring
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        //线程池关闭时 等待任务完成
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //线程池关闭时 最大等待时间
        executor.setAwaitTerminationSeconds(300);
        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }
}


