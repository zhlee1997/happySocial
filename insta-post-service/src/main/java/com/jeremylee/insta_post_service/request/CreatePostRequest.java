package com.jeremylee.insta_post_service.request;

import lombok.Data;

@Data
public class CreatePostRequest {
    private String content;
    private String imageUrl;
//    private Long userId;
}
