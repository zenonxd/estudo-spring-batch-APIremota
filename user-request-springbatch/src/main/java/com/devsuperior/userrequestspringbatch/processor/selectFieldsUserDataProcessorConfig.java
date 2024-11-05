package com.devsuperior.userrequestspringbatch.processor;

import com.devsuperior.userrequestspringbatch.dto.UserDTO;
import com.devsuperior.userrequestspringbatch.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class selectFieldsUserDataProcessorConfig  {

    private static Logger logger = LoggerFactory.getLogger(selectFieldsUserDataProcessorConfig.class);

    private int counter;

    @Bean            //esse UserDTO ele obtém através do ItemReader e retorna um User
    public ItemProcessor<UserDTO, User> selectFieldsUserDataProcessor() {
        return new ItemProcessor<UserDTO, User>() {
            @Override
            public User process(UserDTO item) throws Exception {
                User user = new User();
                user.setLogin(item.getLogin());
                user.setName(item.getName());
                user.setAvatarUrl(item.getAvatarUrl());
                logger.info("[PROCESSOR STEP] select user fields " + counter + user);
                counter++;
                return user;
            }
        };
    }
}
