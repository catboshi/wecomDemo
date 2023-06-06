package tech.wedev.wecom.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import tech.wedev.wecom.utils.SM4Util;
import tech.wedev.wecom.utils.StringUtils;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Data
@Slf4j
@Configurable
@PropertySource(value = "classpath:META-INF/global-config.properties")
public class DataSourceConfig {
    /**
     * 密钥
     */
    @Value("${sm4.key}")
    private String key;
    @Value("${sm4.switch}")
    private String keySwitch;

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
    public DataSource dataSourceConfigBean() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setQueryTimeout(60);
        String decryptPassword;
        dataSource.setFilters("!stat");
        decryptPassword = SM4Util.decryptEcb(SM4Util.hexKey, password);
        if (StringUtils.isBlank(decryptPassword)) {
            log.error("decrypt dataaSource cypher result null");
            throw new IllegalArgumentException("decrypt dataSource cypher result null, please check argument is correct");
        }

        dataSource.setPassword(decryptPassword);
        return dataSource;
    }

}
