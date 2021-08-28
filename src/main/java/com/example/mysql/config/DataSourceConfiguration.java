package com.example.mysql.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.example.mysql.MyRoutingDataSource;
import com.example.mysql.enums.DBTypeEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiahui on 2018/3/14.
 */

/***
 *
 * DataSource 配置
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfiguration {

    @Primary
    @Bean(name = "dataSourceMaster")
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource dataSourceMaster() {

        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceSlave")
    @ConfigurationProperties("spring.datasource.druid.slave")
    public DataSource dataSourceSlave() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public DataSource myRoutingDataSource(@Qualifier("dataSourceMaster") DataSource masterDataSource,
                                          @Qualifier("dataSourceSlave") DataSource slaveDataSource){
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.MASTER, masterDataSource);
        targetDataSources.put(DBTypeEnum.SLAVE, slaveDataSource);
        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        return myRoutingDataSource;
    }
}

