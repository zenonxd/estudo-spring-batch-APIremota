package com.devsuperior.userrequestspringbatch.reader;

import com.devsuperior.userrequestspringbatch.domain.ResponseUser;
import com.devsuperior.userrequestspringbatch.dto.UserDTO;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FetchUserDataReaderConfig implements ItemReader<UserDTO> {

    private final String BASE_URL = "http://localhost:8081";
    private RestTemplate restTemplate = new RestTemplate();

    private int page = 0;

    private List<UserDTO> users = new ArrayList<>();
    private int userIndex = 0;

    @Value("${chunkSize}")
    private int chunkSize;

    @Value("${pageSize}")
    private int pageSize;

    @Override
    public UserDTO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        UserDTO user;

        if (userIndex < users.size()) {
            user = users.get(userIndex);
        } else {
            user = null;
        }

        userIndex++;

        return user;
    }

    private List<UserDTO> fetchUserDataFromAPI() {


        //colocamos %d nos valores pois iremos indicar eles
        String uri = BASE_URL + "/clients/pagedData?page=%d&size=%d";

        //obtendo a entidade
        ResponseEntity<ResponseUser> response = restTemplate.exchange(String.format(uri, getPage(), pageSize),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseUser>() {
                });
        List<UserDTO> result = response.getBody().getContent();
        return result;
    }

    public int getPage() {
        return page;
    }

    public void incrementPage() {
        this.page++;
    }

    @BeforeChunk
    public void beforeChunk(ChunkContext context) {
        //usamos o chunkSize pois para carregar a lista de User
        //queremos carregar com o tamanho do chunkSize

        //o i será incrementado de acordo com o pageSize,
        //pois o pageSize pode ser 10 e o chunk 5
        for (int i = 0; i < chunkSize; i += pageSize) {
            users.addAll(fetchUserDataFromAPI());
        }
    }

    @AfterChunk
    public void afterChunk(ChunkContext context) {
        incrementPage();
        //colocamos o index do user em 0, pois, após trocar de página,
        //ele precisa resetar
        userIndex = 0;

        //zerando também a lista de User
        users.clear();
    }
}
