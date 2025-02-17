package com.jeremylee.insta_auth_service.dto;

import lombok.Data;

@Data
public class UserRegistrationRequestDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
