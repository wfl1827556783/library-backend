package com.library.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private UserDTO user;
    
    public LoginResponseDTO(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }
}



