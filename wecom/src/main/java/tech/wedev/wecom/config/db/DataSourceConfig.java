package tech.wedev.wecom.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import tech.wedev.wecom.utils.SM4Util;
import tech.wedev.wecom.utils.StringUtils;

import javax.sql.DataSource;

@Data
@Slf4j
@Configuration
@PropertySource(value = "classpath:META-INF/global-config.properties")
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String userName;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    @SneakyThrows
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setQueryTimeout(60);
        dataSource.setFilters("!stat");
        String decryptPassword = SM4Util.decryptEcb(SM4Util.hexKey, password);
        if (StringUtils.isBlank(decryptPassword)) {
            log.error("decrypt dataSource cypher result null");
            throw new IllegalArgumentException("decrypt dataSource cypher result null, please check argument is correct");
        }

        dataSource.setPassword(decryptPassword);
        return dataSource;
    }

    @Bean("wecomJdbcTemplate")
    public JdbcTemplate wecomJdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }
}
