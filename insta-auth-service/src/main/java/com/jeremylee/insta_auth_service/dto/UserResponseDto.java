package com.jeremylee.insta_auth_service.dto;

public class UserResponseDto {

    private String message;
    private UserTemplateDto data;

    public UserResponseDto() {}

    public UserResponseDto(String message, UserTemplateDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserTemplateDto getData() {
        return data;
    }

    public void setData(UserTemplateDto data) {
        this.data = data;
    }

}
