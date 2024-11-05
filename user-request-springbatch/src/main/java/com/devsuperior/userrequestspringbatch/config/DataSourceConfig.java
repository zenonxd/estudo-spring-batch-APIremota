package com.devsuperior.userrequestspringbatch.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    //precisamos colocar esse DataSource como padrão, colocamos o Primary
    @Primary
    @Bean
    //associaremos o DataSource aos dados de config do application.properties
    //(o do banco principal, spring_batch)
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource springBatchDB() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.datasource")
    public DataSource appDB() {
        return DataSourceBuilder.create().build();
    }

    //gerenciador de transacoes

    @Bean
    public DataSourceTransactionManager transactionManagerApp(@Qualifier("appDB") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "appJdbcTemplate")
    public JdbcTemplate appJdbcTemplate(@Qualifier("appDB") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
