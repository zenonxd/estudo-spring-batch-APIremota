package com.devsuperior.api.clients.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class fetchUserDataAndStoreDBStepConfig {

    @Bean
    public Step fetchUserDataAndStoreDBStep(ItemReader<> JobRepository jobRepository) {

        return new StepBuilder("fetchUserDataAndStoreDBStep", jobRepository)
                .chunk()

    }
}
