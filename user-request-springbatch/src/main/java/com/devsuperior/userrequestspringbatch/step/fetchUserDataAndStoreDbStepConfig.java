package com.devsuperior.userrequestspringbatch.step;

import com.devsuperior.userrequestspringbatch.dto.UserDTO;
import com.devsuperior.userrequestspringbatch.entities.User;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class fetchUserDataAndStoreDbStepConfig {

    @Autowired
    @Qualifier("transactionManagerApp")
    private PlatformTransactionManager transactionManager;

    @Value("${chunkSize}")
    private int chunkSize;

    @Bean
    public Step fetchUserDataAndStoreDbStep(ItemReader<UserDTO> fetchUserDataReader,
                                            ItemProcessor<UserDTO, User> selectFieldsUserDataProcessor,
                                            ItemWriter<User> insertUserDataDBWriter,
                                            JobRepository jobRepository) {

        return new StepBuilder("fetchUserDataAndStoreDbStep", jobRepository)
                .<UserDTO, User>chunk(chunkSize, transactionManager)
                .reader(fetchUserDataReader)
                .processor(selectFieldsUserDataProcessor)
                .writer(insertUserDataDBWriter)
                .build();
    }
}
