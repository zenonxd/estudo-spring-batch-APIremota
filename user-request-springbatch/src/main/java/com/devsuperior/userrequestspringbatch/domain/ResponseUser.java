package com.devsuperior.userrequestspringbatch.domain;

import com.devsuperior.userrequestspringbatch.dto.UserDTO;

import java.util.List;

public class ResponseUser {

    //content, pois a lista do Postman tem esse nome
    private List<UserDTO> content;

    public List<UserDTO> getContent() {
        return content;
    }
}
