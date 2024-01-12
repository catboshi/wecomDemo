package tech.wedev.wecom.aop;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 内存使用情况及执行耗时统计切面
 * <p>
 * 仅排查问题时作为参考，具体分析请使用jConsole或VisualVM
 */
@Slf4j
@Aspect
@Component
public class StopWatchAop {

//    避免在方法内部调用，否则会失效
//    @Pointcut("@annotation(tech.wedev.wecom.annos.StopWatch)")
//    public void pointCut() {
//
//    }

//    @Around("pointCut()")
    @SneakyThrows
    @Around(value = "@annotation(tech.wedev.wecom.annos.StopWatch)")
    public Object doAround(ProceedingJoinPoint pjp) {
        Object proceed;
        String className;
        String methodName;
        className = pjp.getTarget().getClass().getName();
        methodName = pjp.getSignature().getName();
        long start = System.nanoTime();
        var runtime = Runtime.getRuntime();
        var beforeRAM = runtime.totalMemory() / 1024 / 1024;

        proceed = pjp.proceed();

        long end = System.nanoTime();
        var afterRAM = runtime.freeMemory() / 1024 / 1024;
        var heapSize = runtime.maxMemory() / 1024 / 1024;
        log.info("应用类名：" + className + "，方法名：" + methodName + "，线程名：" + Thread.currentThread().getName() + "，duration耗时：" + String.format("%.2fs", (end - start) * 1e-9));
        log.info("测试RAM结束，堆内存空间约为 : " + (beforeRAM - afterRAM) + "M" + "，Java Heap Size: " + heapSize + "M");
        return proceed;
    }

}