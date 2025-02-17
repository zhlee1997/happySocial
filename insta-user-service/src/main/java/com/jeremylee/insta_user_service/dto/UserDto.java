package com.jeremylee.insta_user_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userId;
    private String role;

}
