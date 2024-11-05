package com.devsuperior.userrequestspringbatch.writer;

import com.devsuperior.userrequestspringbatch.entities.User;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class InsertUserDataDBWriterConfig {

    @Bean
    public ItemWriter<User> insertUserDataDBWriter(@Qualifier("appDB") DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<User>()
                .dataSource(dataSource)
                                                                        //acessamos os valores com o ":"
                .sql("INSERT INTO tb_user (login, name, avatar_url) VALUES (:login, :name, :avatarUrl)")
                //fonte do provider
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }
}
