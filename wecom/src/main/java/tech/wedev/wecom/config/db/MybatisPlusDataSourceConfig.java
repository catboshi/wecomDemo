package tech.wedev.wecom.config.db;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tech.wedev.wecom.utils.SM4Util;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
@MapperScan(basePackages = {"tech.wedev.wecom.mybatisplus.mapper"}, sqlSessionTemplateRef = "mybatisPlusSqlSessionTemplate")
public class MybatisPlusDataSourceConfig {

    @Value("${spring.datasource.mybatis-plus.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.mybatis-plus.jdbc-url}")
    private String jdbcUrl;

    @Value("${spring.datasource.mybatis-plus.username}")
    private String userName;

    @Value("${spring.datasource.mybatis-plus.password}")
    private String password;

    @Bean(name = "mybatisPlusDataSource")
    //@ConfigurationProperties注解获取配置文件里的参数，这里需要对密码进行解密
    //@ConfigurationProperties(prefix = "spring.datasource.mybatis-plus")
    public DataSource dataSource() {
        return DataSourceBuilder.create().driverClassName(driverClassName).url(jdbcUrl).username(userName).password(SM4Util.decryptEcb(SM4Util.hexKey, password)).build();
    }

    @Bean(name = "mybatisPlusSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("mybatisPlusDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //设置mybatis全局变量 dbType
        Properties properties = new Properties();
//        properties.put("dbType", "oracle");
        properties.put("dbType", "mysql");
        bean.setConfigurationProperties(properties);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        //全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        //mybatis-plus 自动填充
        globalConfig.setMetaObjectHandler(new MyMetaObjectHandler());
        //主键自增策略
        globalConfig.setDbConfig(new GlobalConfig.DbConfig().setKeyGenerator(new OracleKeyGenerator()));
        bean.setGlobalConfig(globalConfig);
        return bean.getObject();
    }

    @Bean(name = "mybatisPlusTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("mybatisPlusDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "mybatisPlusSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("mybatisPlusSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
