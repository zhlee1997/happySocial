package com.jeremylee.insta_post_service.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageUploadMessage {
    private String fileName;
    private String filePath;
    private String contentType;
}
