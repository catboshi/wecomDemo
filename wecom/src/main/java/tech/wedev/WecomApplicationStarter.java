package tech.wedev;

import com.alibaba.fastjson.parser.ParserConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@MapperScan("tech.wedev.wecom.dao")
@ImportResource(locations = "classpath:provider.xml")
@SpringBootApplication(scanBasePackages = "tech.wedev")
@EnableTransactionManagement
public class WecomApplicationStarter extends SpringBootServletInitializer {

    /**
     *用于支持通过外部容器方式启动
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        ParserConfig parserConfig = ParserConfig.getGlobalInstance();
        parserConfig.setAutoTypeSupport(true);
        parserConfig.setSafeMode(true);
        return builder.sources(WecomApplicationStarter.class);
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ParserConfig parserConfig = ParserConfig.getGlobalInstance();
        parserConfig.setAutoTypeSupport(true);
        parserConfig.setSafeMode(true);
        SpringApplication.run(WecomApplicationStarter.class, args);
        long endTime = System.currentTimeMillis();
        long cost = (endTime - startTime) / 1000;
        log.info(String.format("*******************%s has been started successfully and it takes %s seconds***************", "WECOM", String.valueOf(cost)));
    }


}
