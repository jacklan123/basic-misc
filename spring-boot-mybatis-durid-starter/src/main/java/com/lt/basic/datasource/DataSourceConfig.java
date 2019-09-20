package com.lt.basic.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lantian
 * @date 2019/09/04
 */
@Configuration
public class DataSourceConfig {


    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);


    @Bean(name = "masterDS")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDS() {

        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();

        try {
            druidDataSource.setFilters("stat, wall");
        } catch (SQLException e) {
            LOGGER.error("", e);
        }

        return druidDataSource;
    }


    @Bean(name = "slaveDS")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDS() {
        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();

        try {
            druidDataSource.setFilters("stat, wall");
        } catch (SQLException e) {
            LOGGER.error("", e);
        }

        return druidDataSource;
    }



    @Bean(name = "dynamicDS")
    public DynamicDataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(masterDS());

        // 配置多数据源
        Map<Object, Object> dsMap = new HashMap(5);
        dsMap.put(DataSourceContextHolder.MASTER_KEY, masterDS());
        dsMap.put(DataSourceContextHolder.SLAVE_KEY, slaveDS());

        dynamicDataSource.setTargetDataSources(dsMap);

        return dynamicDataSource;
    }


}
