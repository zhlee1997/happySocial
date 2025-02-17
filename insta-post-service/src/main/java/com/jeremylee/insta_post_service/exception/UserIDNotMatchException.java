package com.jeremylee.insta_post_service.exception;

public class UserIDNotMatchException extends RuntimeException {
    public UserIDNotMatchException(String message) {
        super(message);
    }

}
